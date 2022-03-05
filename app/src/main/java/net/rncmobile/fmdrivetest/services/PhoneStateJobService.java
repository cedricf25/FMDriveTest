package net.rncmobile.fmdrivetest.services;

import android.app.job.JobParameters;
import android.app.job.JobService;

import net.rncmobile.fmdrivetest.FMDApp;
import net.rncmobile.fmdrivetest.di.component.DaggerServiceComponent;
import net.rncmobile.fmdrivetest.di.component.ServiceComponent;
import net.rncmobile.fmdrivetest.managers.RadioManager;
import net.rncmobile.fmdrivetest.utils.Utils;

import javax.inject.Inject;

public class PhoneStateJobService extends JobService {
    private static final String TAG = "PhoneStateJobService";

    @Inject
    RadioManager mRadioManager;

    @Override
    public void onCreate() {
        super.onCreate();

        ServiceComponent component = DaggerServiceComponent.builder()
                .applicationComponent(((FMDApp) getApplication()).getComponent())
                .build();
        component.inject(this);
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        // Refresh API
        mRadioManager.refreshApi();

        Utils.scheduleJob(getApplicationContext()); // reschedule the job
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }
}
