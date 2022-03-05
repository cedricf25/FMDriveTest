package net.rncmobile.fmdrivetest.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import net.rncmobile.fmdrivetest.managers.RadioManager;

/**
 * Created by cedric_f25 on 05.03.2018.
 */

public class PlaneModeReceiver extends BroadcastReceiver {
    private static final String TAG = "PlaneModeReceiver";

    RadioManager mRadioManager;

    public PlaneModeReceiver(RadioManager radioManager) {
        mRadioManager = radioManager;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Thread.sleep(3000);
            mRadioManager.refreshApi();
        } catch (Exception e) {}
    }

}
