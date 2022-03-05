package net.rncmobile.fmdrivetest.utils;

import android.Manifest.permission;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class PermissionMgr {
    private List<String> permissions = new ArrayList<>();

    public PermissionMgr() {
        permissions.add(permission.ACCESS_FINE_LOCATION);
        permissions.add(permission.ACCESS_COARSE_LOCATION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) permissions.add(permission.ACCESS_BACKGROUND_LOCATION);
        permissions.add(permission.READ_PHONE_STATE);
    }

    // 0 Ok
    // 1 Obligatoire
    // 2 Secondaire
    public boolean checkPermission(Context context) {
        boolean ok = true;
            for (String permission : permissions)
                if ((ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED))
                    ok = false;
        return ok;
    }

    public void requestPermission(Activity activity) {
        for (String permission : permissions)
            if ((ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED))
                ActivityCompat.requestPermissions(activity, new String[]{permission}, AppConstants.REQUEST_CODE_ASK_PERMISSIONS);
    }
}
