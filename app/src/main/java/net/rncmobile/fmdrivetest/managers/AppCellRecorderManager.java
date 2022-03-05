package net.rncmobile.fmdrivetest.managers;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.BatteryManager;
import android.util.Log;

import net.rncmobile.fmdrivetest.data.DataManager;
import net.rncmobile.fmdrivetest.data.db.model.CellRecorder;
import net.rncmobile.fmdrivetest.di.ApplicationContext;
import net.rncmobile.fmdrivetest.listeners.FusedLocationListener;
import net.rncmobile.fmdrivetest.models.MyTelephonyFactory;
import net.rncmobile.fmdrivetest.models.cells.IMyCell;
import net.rncmobile.fmdrivetest.utils.AppConstants;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.subjects.PublishSubject;

@Singleton
public class AppCellRecorderManager implements CellRecorderManager, FusedLocationListener.Listener {
    private static final String TAG = "AppCRManager";

    @Inject
    DataManager dataManager;

    RadioManager radioManager;

    private final Context context;

    private FusedLocationListener fusedLocationListener;

    private boolean fakeGps = false;
    private boolean enabled = false;

    private final PublishSubject<Boolean> crStatusSubject;

    @Inject
    AppCellRecorderManager(@ApplicationContext Context context,
                           RadioManager radioManager) {
        this.context = context;
        this.radioManager = radioManager;

        crStatusSubject = PublishSubject.create();
    }

    @Override
    public PublishSubject<Boolean> registerOnCRInserted() {
        return crStatusSubject;
    }

    @Override
    public void getCRStatus() {
        crStatusSubject.onNext(enabled);
    }

    @Override
    public boolean isCrEnabled() {
        return enabled;
    }

    @Override
    public void startCellRecorder() {
        if(!AppConstants.cellRecorder) {
            AppConstants.cellRecorder = true;
            fusedLocationListener = new FusedLocationListener(context, 8, 8000);

            if (fusedLocationListener.isGpsEnabled()) {
                fusedLocationListener.setListener(this);
                fusedLocationListener.startGps();
                enabled = true;
                radioManager.setCrEnabled(true);

                radioManager.refreshCellChangeObservers();

            } else enabled = false;

            crStatusSubject.onNext(enabled);
        }
    }

    @Override
    public void stopCellRecorder() {
        AppConstants.cellRecorder = false;
        if (fusedLocationListener != null) fusedLocationListener.stop();

        enabled = false;
        if(radioManager != null) radioManager.setCrEnabled(false);
        crStatusSubject.onNext(enabled);
    }

    @Override
    public void onNewLocationAvailable(Location location) {
        try {
            IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryStatus = context.registerReceiver(null, ifilter);

            int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

            float batLevel = (float) level / (float) scale;
            int batLevelPercent = (int) (batLevel * 100);

            if (MyTelephonyFactory.getInstance().get(context).isPlaneMode() || !fusedLocationListener.isGpsEnabled() || fakeGps) {
                stopCellRecorder();
                return;
            }

            if (enabled && radioManager.getCurrentCell() != null && radioManager.getCurrentCell().isAuthorizedNetwork() && location.getAccuracy() < 50.0) {
                CellRecorder cellRecorder = new CellRecorder(radioManager.getCurrentCell(), location.getLatitude(), location.getLongitude(),
                            location.getAltitude(), location.getAccuracy(), location.getSpeed(), batLevelPercent);

                    int nbAggreg = 0;
                    List<IMyCell> nCells = radioManager.getnCells();
                    for (int i = 0; i < nCells.size(); i++) {
                        if (nCells.get(i).getPxx() == radioManager.getCurrentCell().getPxx()) {
                            if (nbAggreg == 0) cellRecorder.setAggreg1(nCells.get(i));
                            if (nbAggreg == 1) cellRecorder.setAggreg2(nCells.get(i));
                            if (nbAggreg == 2) cellRecorder.setAggreg3(nCells.get(i));

                            nbAggreg++;
                        }
                    }

                    AppConstants.cellRecorder = true;

                    dataManager.addCellRecorder(cellRecorder);
                    crStatusSubject.onNext(enabled);

            }
        } catch (Exception e) {
            AppConstants.cellRecorder = false;
        }
    }

    @Override
    public void onMockLocationsDetected() {
        fakeGps = true;
    }

    @Override
    public void onFirstFix() {
    }
}
