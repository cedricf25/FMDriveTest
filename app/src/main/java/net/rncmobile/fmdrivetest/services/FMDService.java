package net.rncmobile.fmdrivetest.services;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.Nullable;

import net.rncmobile.fmdrivetest.FMDApp;
import net.rncmobile.fmdrivetest.data.DataManager;
import net.rncmobile.fmdrivetest.di.VersionInfo;
import net.rncmobile.fmdrivetest.di.component.DaggerServiceComponent;
import net.rncmobile.fmdrivetest.di.component.ServiceComponent;
import net.rncmobile.fmdrivetest.managers.RadioManager;
import net.rncmobile.fmdrivetest.ui.notification.NotificationBar;
import net.rncmobile.fmdrivetest.utils.rx.AppSchedulerProvider;
import net.rncmobile.fmdrivetest.utils.rx.SchedulerProvider;

import java.text.SimpleDateFormat;
import java.util.concurrent.ScheduledExecutorService;

import javax.inject.Inject;

/**
 * Created by cedric_f25 on 27.11.2017.
 */

public class FMDService extends Service {
    private static final String TAG = "FMDService";

    private ServiceComponent mServiceComponent;
    private ScheduledExecutorService scheduleTaskExecutor;

    @Inject
    RadioManager mRadioManager;
    @Inject
    @VersionInfo
    String appVersion;

    PowerManager.WakeLock mWakeLock;

    SchedulerProvider schedulerProvider = new AppSchedulerProvider();

    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void onCreate() {
        super.onCreate();

        mServiceComponent = DaggerServiceComponent.builder()
                .applicationComponent(((FMDApp) getApplication()).getComponent())
                .build();
        mServiceComponent.inject(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
        if (mWakeLock != null && !mWakeLock.isHeld())
            mWakeLock.acquire(30*60*1000L /*30 minutes*/);

        NotificationBar.getInstance().run(getApplicationContext());
        Log.d(TAG, NotificationBar.getInstance().notification.toString());
        startForeground(1, NotificationBar.getInstance().notification);

        mRadioManager.refreshApi();

        return START_STICKY;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        Log.d(TAG, "onRebind");
        super.onRebind(intent);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.d(TAG, "onTaskRemoved");
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
        //mRadioManager.stopCellRecorder();

        if (mWakeLock != null && mWakeLock.isHeld())
            mWakeLock.release();

        if(scheduleTaskExecutor != null)
            scheduleTaskExecutor.shutdown();
        stopForeground(true);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
