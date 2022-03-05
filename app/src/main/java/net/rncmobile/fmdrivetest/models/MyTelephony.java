package net.rncmobile.fmdrivetest.models;

import static android.content.Context.CONNECTIVITY_SERVICE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.ServiceState;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.RequiresApi;

import net.rncmobile.fmdrivetest.models.cells.IMyCell;
import net.rncmobile.fmdrivetest.models.cells.MyCell;
import net.rncmobile.fmdrivetest.utils.AppConstants;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cz.mroczis.netmonster.core.INetMonster;
import cz.mroczis.netmonster.core.db.model.NetworkType;
import cz.mroczis.netmonster.core.factory.NetMonsterFactory;
import cz.mroczis.netmonster.core.feature.detect.DetectorLteAdvancedNrServiceState;
import cz.mroczis.netmonster.core.feature.merge.CellSource;
import cz.mroczis.netmonster.core.model.cell.ICell;
import cz.mroczis.netmonster.core.telephony.ITelephonyManagerCompat;

public class MyTelephony implements IMyTelephony {
    private static final String TAG = "MyTelephony";

    private Context context;
    private int subscriptionId;

    private TelephonyManager tm;
    private INetMonster netMonster;
    private ITelephonyManagerCompat telephonyManagerCompat;
    private ServiceState state;
    private ConnectivityManager connectivityManager;
    private List<SubscriptionInfo> subInfoList;

    private NetworkType nt2;

    MyTelephony(Context context) {
        this.context = context;

        SharedPreferences mPrefs = context.getSharedPreferences(AppConstants.PREF_NAME, Context.MODE_PRIVATE);

        tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        netMonster = NetMonsterFactory.INSTANCE.get(context);
        telephonyManagerCompat = NetMonsterFactory.INSTANCE.getTelephony(context, Integer.parseInt(mPrefs.getString("nb_sim", "1")));
        state = telephonyManagerCompat.getServiceState();

        try {
            SubscriptionManager subscriptionInfo;
            subscriptionInfo = (SubscriptionManager) context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
            if (subscriptionInfo != null)
                subInfoList = subscriptionInfo.getActiveSubscriptionInfoList();
        } catch (SecurityException e) {
            Log.d(TAG, "Erreur: " + e.toString());
        }

        connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
    }

    @Override
    public List<IMyCell> getCells(int subsricptionId) {
        this.subscriptionId = subsricptionId;

        telephonyManagerCompat = NetMonsterFactory.INSTANCE.getTelephony(context, subsricptionId);
        state = telephonyManagerCompat.getServiceState();

        List<ICell> lAci;
        List<IMyCell> myCells = new ArrayList<>();

        try {
            NetworkType nt2 = netMonster.getNetworkType(subsricptionId, new DetectorLteAdvancedNrServiceState());

            lAci = netMonster.getCells(CellSource.ALL_CELL_INFO);
            int tech = -1;

            // Nous devons vérifier la 5G NSA manuelement car pour le moment elle n'est pas reporteé dans les cellInfo
            if(nt2 != null) {
                if(this.isNrAvailable()) {
                    if(this.isNsaConnected()) tech = Integer.MAX_VALUE - 3;
                }
            }

            // Mapping des cells
            for (ICell iCell : lAci) {
                IMyCell myCell = new MyCell();

                myCell.setTechnology(tech);

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    if (this.state != null && this.state.getCellBandwidths() != null)
                        myCell.setBw(this.state.getCellBandwidths());
                }// else myCell.setBw([]);

                myCell.setICell(iCell);

                myCells.add(myCell);

                myCell.setMncOpe(Integer.parseInt(Objects.requireNonNull(telephonyManagerCompat.getSimOperator()).getMnc()));
            }

        } catch (SecurityException e) {
            Log.d("MyTelephony", "erreur permission:" + e.toString());
        } catch (Exception e) {
            Log.d("MyTelephony", "erreur:" + e.toString());
        }

        return myCells;
    }

    @Override
    public String getNetworkOperatoName() {
        try {
            return telephonyManagerCompat.getTelephonyManager().getNetworkOperatorName();
        } catch (Exception e) {
            return "-";
        }
    }

    @Override
    public boolean isNrAvailable() {
        if(this.subscriptionId != 0) {
            NetworkType nt2 = netMonster.getNetworkType(this.subscriptionId, new DetectorLteAdvancedNrServiceState());
            if (nt2 != null) {
                if (nt2.toString().contains("nrAvailable=true")) return true;
            }
        }
        return false;
    }

    @Override
    public boolean isNsaConnected() {
        if(this.subscriptionId != 0) {
            NetworkType nt2 = netMonster.getNetworkType(this.subscriptionId, new DetectorLteAdvancedNrServiceState());
            if (nt2 != null) {
                if (nt2.toString().contains("connection=Connected")) return true;
            }
        }
        return false;
    }

    @SuppressLint("HardwareIds")
    @Override
    public String getDeviceId() {
        String deviceId = "error";
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (SecurityException e) {
            return "SecurityException error";
        } catch (Exception e) {
            return "Exception error";
        }

        try {
            if (deviceId != null) {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(deviceId.getBytes());
                byte[] digest = md.digest();
                StringBuffer sb = new StringBuffer();
                for (byte b : digest) {
                    sb.append(String.format("%02x", b & 0xff));
                }
                return sb.toString();
            }
        } catch (NoSuchAlgorithmException e) {
            return "hash error";
        }
        return deviceId;
    }

    @Override
    public ServiceState getServiceState() {
        return state;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public String getBandWidth(int currentBandwith) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && this.state != null && this.state.getCellBandwidths() != null) {
            StringBuilder bandwidth = new StringBuilder(String.valueOf(currentBandwith / 1000));
            int length = this.state.getCellBandwidths().length;
            if (length >= 2) bandwidth.append("+");

            for (int i = 0; i < length; i++) {
                if (currentBandwith == this.state.getCellBandwidths()[i]) continue;
                if (i != length - 1)
                    bandwidth.append(this.state.getCellBandwidths()[i] / 1000).append("+");
                else bandwidth.append(this.state.getCellBandwidths()[i] / 1000);
            }
            return bandwidth + " MHz";
        } else return "-";
    }

    public boolean isDualSim() {
        List<SubscriptionInfo> subInfoList;
        try {
            SubscriptionManager subscriptionInfo = (SubscriptionManager) context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
            if (subscriptionInfo != null) {
                subInfoList = subscriptionInfo.getActiveSubscriptionInfoList();
                if (subInfoList != null && subInfoList.size() > 1)
                    return true;
            }
        } catch (SecurityException e) {
            Log.d(TAG, "Erreur sécurité:" + e.toString());
        }
        return false;
    }

    @Override
    public boolean isPlaneMode() {
        return Settings.Global.getInt(context.getContentResolver(),
                Settings.Global.AIRPLANE_MODE_ON, 0) == 1;
    }

    @Override
    public boolean isWifiEnabled() {
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) return false;
        return (networkInfo.getType() == ConnectivityManager.TYPE_WIFI);
    }

    @Override
    public String getWifiSSID() {
        if (isWifiEnabled()) {
            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            assert wifiManager != null;
            WifiInfo info = wifiManager.getConnectionInfo();
            return info.getSSID();
        }
        return "-";
    }

    @Override
    public boolean isVpnEnabled() {
        if (connectivityManager.getActiveNetworkInfo() == null) return false;
        return (connectivityManager.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_VPN);
    }

    @Override
    public boolean isNetworkConnected() {
        try {
            if (connectivityManager == null) return false;
            return Objects.requireNonNull(connectivityManager.getActiveNetworkInfo()).isConnected();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isIpv6Address() {
        try {
            Inet6Address dest = (Inet6Address) InetAddress.getByName("rncmobile.net");
            Socket socket = new Socket(dest, 443);
            return true;
        } catch (Exception ex) {
            Log.e("IP v6", ex.toString());
        }

        return false;
    }

    @Override
    public TelephonyManager getTelephonyManager() {
        return telephonyManagerCompat.getTelephonyManager();
    }
}
