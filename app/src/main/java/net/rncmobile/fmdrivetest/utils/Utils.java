package net.rncmobile.fmdrivetest.utils;

import android.app.ProgressDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import net.rncmobile.fmdrivetest.R;
import net.rncmobile.fmdrivetest.services.PhoneStateJobService;

/**
 * Created by cedricf_25 on 09/10/2015.
 */
final public class Utils {
    private static final String TAG = "Utils";

    // schedule the start of the service every 10 minutes
    public static void scheduleJob(Context context) {
        try {
            ComponentName serviceComponent = new ComponentName(context, PhoneStateJobService.class);
            JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
            builder.setMinimumLatency(30 * 60 * 1000); // wait at least
            builder.setOverrideDeadline(60 * 60 * 1000); // maximum delay
            JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
            jobScheduler.schedule(builder.build());
        } catch (Exception e) {
        }
    }

    // kill job scheduler
    public static void killScheduleJob(Context context) {
        try {
            JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
            jobScheduler.cancelAll();
        } catch (Exception e) {
        }
    }

    public static ProgressDialog showLoadingDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.show();
        if (progressDialog.getWindow() != null) {
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        return progressDialog;
    }

}
