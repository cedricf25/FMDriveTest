package net.rncmobile.fmdrivetest.managers;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import net.rncmobile.fmdrivetest.data.DataManager;
import net.rncmobile.fmdrivetest.data.prefs.PreferencesHelper;
import net.rncmobile.fmdrivetest.di.ApplicationContext;
import net.rncmobile.fmdrivetest.models.MyTelephonyFactory;
import net.rncmobile.fmdrivetest.models.cells.IMyCell;
import net.rncmobile.fmdrivetest.models.cells.MyCell;
import net.rncmobile.fmdrivetest.ui.notification.NotificationBar;
import net.rncmobile.fmdrivetest.utils.AppConstants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Singleton;

import cz.mroczis.netmonster.core.model.connection.PrimaryConnection;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;

/**
 * Created by cedric_f25 on 27.12.2017.
 */

@Singleton
public class AppRadioManager implements RadioManager {
    private static final String TAG = "AppRadioManager";

    @Inject
    DataManager dm;
    @Inject
    PreferencesHelper mPrefs;

    boolean isCrEnabled = false;

    private final Context context;
    private final PublishSubject<IMyCell> signalChangeSubject;
    private final PublishSubject<IMyCell> cellChangeSubject;

    private IMyCell currentCell;
    //private List<ICell> lAci;
    private List<IMyCell> lAci;
    private List<IMyCell> nCells;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRANCE);
    private final Handler handler;

    @Inject
    AppRadioManager(@ApplicationContext Context context) {
        this.context = context;

        signalChangeSubject = PublishSubject.create();
        cellChangeSubject = PublishSubject.create();

        handler = new Handler();
    }

    @Override
    public PublishSubject<IMyCell> registerOnSignalChange() {
        return signalChangeSubject;
    }

    @Override
    public PublishSubject<IMyCell> registerOnCellChange() {
        return cellChangeSubject;
    }

    @Override
    public void refreshSignalChangeObservers() {
        if (currentCell != null) signalChangeSubject.onNext(currentCell);
    }

    @Override
    public void refreshCellChangeObservers() {
        if (currentCell != null) cellChangeSubject.onNext(currentCell);
    }

    @Override
    public IMyCell getCurrentCell() {
        return currentCell;
    }

    @Override
    public void debugCellChange() {
        refreshApi();
    }

    public void AppRadioAsyncTask() {
        CompositeDisposable compositeDisposable = new CompositeDisposable();

        compositeDisposable.add(Observable.fromCallable(() -> {

            try {
                boolean autocomplete = false;

                if (!AppConstants.DEBUG_MODE)
                    lAci = MyTelephonyFactory.getInstance().get(context).getCells(mPrefs.getActiveSim());
                else {
                    lAci = new ArrayList<>();
                    lAci.add(AppConstants.debugCell());
                    lAci.addAll(AppConstants.debugCellSecondary());
                }
                nCells = Collections.synchronizedList(new ArrayList<>());

                if (lAci.size() == 0) signalChangeSubject.onNext(new MyCell());

                for (IMyCell aci : lAci) {
                    if (aci.getICell().getSubscriptionId() == mPrefs.getActiveSim() || AppConstants.DEBUG_MODE) {
                        if (aci.getICell().getConnectionStatus() instanceof PrimaryConnection) {
                            if (aci.getLcid() <= 1) {
                                AppRadioManager.this.stopApi();
                                cellChangeSubject.onNext(new MyCell());
                                handler.postDelayed(updateCellAtFixedRate, 5000);
                                return "";
                            }

                            currentCell = aci;

                            signalChangeSubject.onNext(currentCell);
                        } else nCells.add(aci);
                    }
                }

                for (IMyCell cell : new ArrayList<>(nCells)) {
                    if (currentCell != null && cell.getICell() != null && cell.getICell().getBand() != null
                            && cell.getICell().getBand() != null && !(cell.getICell().getConnectionStatus() instanceof PrimaryConnection)) {

                        if (cell.getTech() == 4) {

                            // Aggregation via la méthode des PCI identique

                        }
                    }
                }

                NotificationBar.getInstance().refresh(context, currentCell);
            } catch (Exception e) {
                return "nok";
            }
            return "ok";
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    //Log.d(TAG, "ok");
                }, (Throwable throwable) -> Log.d(TAG, "Une erreur s'est produite: " + throwable.toString())));
    }

    private final Runnable updateCellAtFixedRate = new Runnable() {
        @Override
        public void run() {
            try {
                AppRadioAsyncTask();
                if (!AppConstants.screenOff || isCrEnabled) handler.postDelayed(this, 3000);
                else handler.postDelayed(this, 20000);
            } catch (Exception ignored) {
            }
        }
    };

    @Override
    public void setCrEnabled(boolean crEnabled) {
        isCrEnabled = crEnabled;
    }

    private void startApi() {
        handler.postDelayed(updateCellAtFixedRate, 100);
    }

    @Override
    public void refreshApi() {
        stopApi();
        currentCell = null;
        startApi();
    }

    public void stopApi() {
        handler.removeCallbacks(updateCellAtFixedRate);
    }

    @Override
    public List<IMyCell> getnCells() {
        return nCells;
    }
}
