package net.rncmobile.fmdrivetest.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import net.rncmobile.fmdrivetest.managers.RadioManager;
import net.rncmobile.fmdrivetest.utils.AppConstants;

/**
 * Created by cedric_f25 on 05.04.2017.
 */

public class ScreenReceiver extends BroadcastReceiver {
    private static final String TAG = "ScreenReceiver";

    RadioManager mRadioManager;

    public ScreenReceiver(RadioManager radioManager) {
        mRadioManager = radioManager;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            AppConstants.screenOff = true;
        }
        else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            AppConstants.screenOff = false;
            mRadioManager.refreshApi();
        }
    }
}
