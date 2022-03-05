package net.rncmobile.fmdrivetest.models;

import android.telephony.ServiceState;
import android.telephony.TelephonyManager;

import net.rncmobile.fmdrivetest.models.cells.IMyCell;

import java.util.List;

public interface IMyTelephony {
    List<IMyCell> getCells(int subsricptionId);

    String getNetworkOperatoName();
    String getDeviceId();
    String getWifiSSID();

    String getBandWidth(int currentBandwith);

    ServiceState getServiceState();

    boolean isNrAvailable();
    boolean isNsaConnected();

    boolean isPlaneMode();
    boolean isWifiEnabled();
    boolean isVpnEnabled();
    boolean isDualSim();
    boolean isNetworkConnected();
    boolean isIpv6Address();

    TelephonyManager getTelephonyManager();
}
