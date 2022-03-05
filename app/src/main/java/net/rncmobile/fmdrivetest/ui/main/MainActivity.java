package net.rncmobile.fmdrivetest.ui.main;

import static net.rncmobile.fmdrivetest.utils.AppConstants.planeModeReceiver;
import static net.rncmobile.fmdrivetest.utils.AppConstants.screenReceiver;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import net.rncmobile.fmdrivetest.R;
import net.rncmobile.fmdrivetest.data.prefs.AppPreferencesHelper;
import net.rncmobile.fmdrivetest.databinding.ActivityMainBinding;
import net.rncmobile.fmdrivetest.models.MyTelephonyFactory;
import net.rncmobile.fmdrivetest.models.cells.IMyCell;
import net.rncmobile.fmdrivetest.receiver.PlaneModeReceiver;
import net.rncmobile.fmdrivetest.receiver.ScreenReceiver;
import net.rncmobile.fmdrivetest.services.FMDService;
import net.rncmobile.fmdrivetest.ui.adapters.GridCellInfoAdapter;
import net.rncmobile.fmdrivetest.ui.base.BaseActivity;
import net.rncmobile.fmdrivetest.ui.settings.SettingsActivity;
import net.rncmobile.fmdrivetest.utils.AppConstants;
import net.rncmobile.fmdrivetest.utils.PermissionMgr;
import net.rncmobile.fmdrivetest.utils.Utils;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import cz.mroczis.netmonster.core.factory.NetMonsterFactory;
import cz.mroczis.netmonster.core.model.SubscribedNetwork;

public class MainActivity extends BaseActivity implements MainMvpView {
    private static final String TAG = "MainActivity";

    private ActivityMainBinding binding;

    @Inject
    MainMvpPresenter<MainMvpView> mPresenter;

    private final String[] INFOS_CELL_2G = new String[]{
            "CID", "ARFCN", "LAC", "RSSI", "BSIC", "TA"};

    private final String[] INFOS_CELL_3G = new String[]{
            "CI", "CID", "LAC", "RNC", "PSC", "RSCP", "ARFCN"};

    private final String[] INFOS_CELL_4G = new String[]{
            "CI", "RSSI", "eNb", "RSRP", "TAC", "RSRQ", "PCI", "SNR", "TA", "EARFCN", "BW"};

    private final String[] INFOS_CELL_5G = new String[]{
            "Nci", "RSSI", "eNb", "RSRP", "TAC", "RSRQ", "PCI", "SNR", "TA", "EARFCN", "BW"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppConstants.mainActivity = this;

        AppPreferencesHelper appPreferencesHelper = new AppPreferencesHelper(getContext(), AppConstants.PREF_NAME);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getActivityComponent().inject(this);

        mPresenter.onAttach(this);

        if (mPresenter != null && mPresenter.prefIsScreen())
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.mainActivityTitle);

        mPresenter.registerOnCellRecorderChange();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void setUp() {
        if (AppConstants.IS_APP_WAS_KILL) {
            AppConstants.IS_APP_WAS_KILL = false;

            IntentFilter planeModeFilter = new IntentFilter("android.intent.action.AIRPLANE_MODE");
            planeModeReceiver = new PlaneModeReceiver(mPresenter.getRadioManager());
            getApplicationContext().registerReceiver(planeModeReceiver, planeModeFilter);

            screenReceiver = new ScreenReceiver(mPresenter.getRadioManager());
            IntentFilter screenStateFilter = new IntentFilter();
            screenStateFilter.addAction(Intent.ACTION_SCREEN_ON);
            screenStateFilter.addAction(Intent.ACTION_SCREEN_OFF);
            getApplicationContext().registerReceiver(screenReceiver, screenStateFilter);

            // Choix de la sim au premier démarrage de l'application
            if( !mPresenter.prefIsSartupSimChoice()) {
                final int[] selectedSim = {-1};

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(false);

                // Get the layout inflater
                LayoutInflater inflater = this.getLayoutInflater();
                View view = inflater.inflate(R.layout.dialog_radio_button, new FrameLayout(this));

                builder.setView(view)
                        .setPositiveButton("Ok", (DialogInterface d, int id) -> {
                            mPresenter.setIsSartupSimChoice(true);
                            mPresenter.setActiveSimsetActiveSim(String.valueOf(selectedSim[0]));
                        })
                        .setNegativeButton("Fermer",  (DialogInterface d, int id) -> {});

                AlertDialog dialog = builder.create();

                dialog.setOnShowListener(dialog1 -> ((AlertDialog) dialog1).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false));

                dialog.show();

                RadioGroup rg = view.findViewById(R.id.radio_group);

                // SIM Managment

                try {
                    List<SubscribedNetwork> subInfoList = NetMonsterFactory.INSTANCE.getSubscription(getContext()).getActiveSubscriptions();
                    for (int i = 0; i < subInfoList.size(); i++) {
                        if (subInfoList.get(i).getNetwork() != null
                                && subInfoList.get(i).getNetwork().getMcc() != null && subInfoList.get(i).getNetwork().getMnc() != null) {
                            String mcc = Objects.requireNonNull(subInfoList.get(i).getNetwork()).getMcc();
                            String mnc = Objects.requireNonNull(subInfoList.get(i).getNetwork()).getMnc();

                            int subID = subInfoList.get(i).getSubscriptionId();

                            RadioButton rb = new RadioButton(this);

                            rb.setText(getString(R.string.simMessage, subID, mcc, mnc));
                            rb.setId(subID);
                            rb.setOnClickListener(v -> {
                                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                                mPresenter.setActiveSimsetActiveSim(String.valueOf(rb.getId()));
                                mPresenter.getRadioManager().refreshApi();
                                selectedSim[0] = rb.getId();
                            });
                            rg.addView(rb);
                        }
                    }
                } catch (SecurityException e) {
                    showMessage(getString(R.string.permissionError));
                } catch (Exception e) {
                    showMessage(getString(R.string.genericError));
                }
            }
        }

        mPresenter.onViewPrepared();

        binding.btnStopAndGo.setOnClickListener(v -> {
            if(AppConstants.IS_COLLECTING) {
                binding.cdMonitorInfosGeneral.setVisibility(View.GONE);
                mPresenter.getRadioManager().stopApi();
                mPresenter.getCellRecorderManager().stopCellRecorder();
                stopService();
                AppConstants.IS_COLLECTING = false;
            } else {
                Intent intentService = new Intent(getApplicationContext(), FMDService.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    startForegroundService(intentService);
                else startService(intentService);

                mPresenter.getCellRecorderManager().startCellRecorder();

                mPresenter.registerOnCellChange();
                mPresenter.registerOnSignalChange();

                Utils.scheduleJob(getApplicationContext());
                AppConstants.IS_COLLECTING = true;
                binding.cdMonitorInfosGeneral.setVisibility(View.VISIBLE);
            }
            if(AppConstants.IS_COLLECTING) {
                binding.btnStopAndGo.setBackgroundColor(getResources().getColor(R.color.ColorOrange, getTheme()));
                binding.btnStopAndGo.setText("Arreter la collecte");
            } else {
                binding.btnStopAndGo.setBackgroundColor(getResources().getColor(R.color.purple_200, getTheme()));
                binding.btnStopAndGo.setText("Démarrer la collecte");
            }
        });

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void refreshMonitor(IMyCell cell) {
        binding.imgPause.setVisibility(View.GONE);

        binding.txtOperator.setText(MyTelephonyFactory.getInstance().get(getContext()).getNetworkOperatoName());

        binding.txtOpeTech.setText(cell.getNetworkTechnoTxt());
        binding.txtMccMnc.setText(cell.getMcc() + "" + cell.getMncOpe() + " (" + cell.getMnc() + ")");
        binding.txtFreq.setText(getString(R.string.unit_mhz, Integer.parseInt(cell.getFreq())));

        binding.txtSect.setText("Secteur " + cell.getSect());

        if(MyTelephonyFactory.getInstance().get(getContext()).isNrAvailable()) {
            binding.txtNsa.setVisibility(View.VISIBLE);
            if (!MyTelephonyFactory.getInstance().get(getContext()).isNsaConnected()) {
                binding.txtNsa.setTextColor(getResources().getColor(R.color.ColorRed, getTheme()));
                binding.txtNsa.setText("NSA Inactive");
            } else {
                binding.txtNsa.setTextColor(getResources().getColor(R.color.ColorGreen, getTheme()));
                binding.txtNsa.setText("NSA Active");
            }
        } else binding.txtNsa.setVisibility(View.GONE);


        if (cell.getTech() == 4 && cell.isLteCa()) {
            binding.txtAgreg.setVisibility(View.VISIBLE);
            binding.txtAgreg.setText("Porteuse agrégée : " + cell.getAgregLteRsrp() + " dBm");
        } else if (cell.getTech() == 4 && cell.isLteCa() && cell.isLteCa2()) {
            binding.txtAgreg.setVisibility(View.VISIBLE);
            binding.txtAgreg.setText("Porteuses agrégées : " + cell.getAgregLteRsrp() + " dBm + " + cell.getAgreg2LteRsrp() + " dBm");
        } else if (cell.getTech() == 4 && cell.isLteCa() && cell.isLteCa2() && cell.isLteCa3()) {
            binding.txtAgreg.setVisibility(View.VISIBLE);
            binding.txtAgreg.setText("Porteuses agrégées : " + cell.getAgregLteRsrp() + " dBm + " + cell.getAgreg2LteRsrp() + " dBm" + cell.getAgreg3LteRsrp() + " dBm");
        } else binding.txtAgreg.setVisibility(View.GONE);


        if (cell.getTech() == 4 && binding.txtOpeTech != null) {
            if (cell.getICell().getBand().getChannelNumber() > 0) {
                if (cell.isLteCa() && !cell.isLteCa2() && !cell.isLteCa3())
                    binding.txtFreq.setText(cell.getFreq() + "+" + cell.getAgregLteEarfcn() + " MHz");
                else if (cell.isLteCa() && cell.isLteCa2() && !cell.isLteCa3() && cell.getAgregLteEarfcn() != null && !cell.getAgregLteEarfcn().contains("null"))
                    binding.txtFreq.setText(cell.getICell().getBand().getName() + "+"
                            + cell.getAgregLteEarfcn() + "+"
                            + cell.getAgreg2LteEarfcn() + " MHz");
                else if (cell.isLteCa() && cell.isLteCa2() && cell.isLteCa3() && cell.getAgregLteEarfcn() != null && !cell.getAgregLteEarfcn().contains("null"))
                    binding.txtFreq.setText(cell.getICell().getBand().getName() + "+"
                            + cell.getAgregLteEarfcn() + "+"
                            + cell.getAgreg2LteEarfcn() + "+"
                            + cell.getAgreg3LteEarfcn() + " MHz");
                else
                    binding.txtFreq.setText(cell.getICell().getBand().getName() + "MHz");
            } else binding.txtFreq.setText(cell.getICell().getBand().getName() + " MHz");
        }

        if (cell.getTech() == 2)
            binding.gvInfosCell.setAdapter(new GridCellInfoAdapter(getContext(), INFOS_CELL_2G, cell));
        else if (cell.getTech() == 3)
            binding.gvInfosCell.setAdapter(new GridCellInfoAdapter(getContext(), INFOS_CELL_3G, cell));
        else if (cell.getTech() == 4)
            binding.gvInfosCell.setAdapter(new GridCellInfoAdapter(getContext(), INFOS_CELL_4G, cell));
        else if (cell.getTech() == 5)
            binding.gvInfosCell.setAdapter(new GridCellInfoAdapter(getContext(), INFOS_CELL_5G, cell));
    }

    @Override
    public void refreshInfos() {
    }

    @Override
    public void setNbCellRecorder(Long nbCellRecorder) {
        binding.txtNbCollect.setText(String.valueOf(nbCellRecorder));
    }

    @Override
    public void displayFullScreenMessage(View.OnClickListener clickListener,int image, String title, String description) {
        binding.lytFullscreenMessage.setVisibility(View.VISIBLE);
        binding.container.setVisibility(View.GONE);

        binding.imgFullscreenMessage.setImageDrawable(ResourcesCompat.getDrawable(getResources(), image, getTheme()));
        binding.txtFullscreenMessageTitle.setText(title);
        binding.txtFullscreenMessageDesc.setText(description);

        if(title.contains("Permissions"))
            binding.btnPermissionOk.setVisibility(View.VISIBLE);

        binding.imgFullscreenMessage.setOnClickListener(clickListener);
        binding.btnPermissionOk.setOnClickListener(clickListener);

        binding.btnPermissionNok.setOnClickListener(b -> {
            finishApp();
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(SettingsActivity.getStartIntent(MainActivity.this));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(!new PermissionMgr().checkPermission(this)) {
            displayFullScreenMessage((View v) -> new PermissionMgr().requestPermission(this),
                    R.drawable.baseline_warning_24_black,
                    getString(R.string.noPermissionTitle), getString(R.string.noPermissionDesc));
        } else setUp();
    }

    @Override
    public void hideFullScreenMessage() {
        if(binding.container.getVisibility() == View.GONE) {
            binding.lytFullscreenMessage.setVisibility(View.GONE);
            binding.container.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void noCell() {
        binding.imgPause.setVisibility(View.VISIBLE);
        binding.imgPause.setImageResource(R.drawable.baseline_pause_24);
    }

    @Override
    public void planeMode() {
        binding.imgPause.setVisibility(View.VISIBLE);
        binding.imgPause.setImageResource(R.drawable.ic_airplanemode_active_black_24dp);
    }

    @Override
    public void noGps() {
        binding.imgPause.setVisibility(View.VISIBLE);
        binding.imgPause.setImageResource(R.drawable.ic_location_off_red_24dp);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(mPresenter != null) mPresenter.onDetach();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        finishApp();
    }

    private void stopService() {
        Intent intentService = new Intent(getApplicationContext(), FMDService.class);
        stopService(intentService);
        Utils.killScheduleJob(getApplicationContext());

        mPresenter.getCellRecorderManager().stopCellRecorder();
        mPresenter.getRadioManager().stopApi();

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    private void finishApp() {
        stopService();
        mPresenter.getRadioManager().stopApi();

        try {
            if (planeModeReceiver != null)
                getApplicationContext().unregisterReceiver(planeModeReceiver);
            if (screenReceiver != null) getApplicationContext().unregisterReceiver(screenReceiver);
        } catch (Exception e) {
            Log.d(TAG, "Erreur générale: " + e.toString());
        }

        AppConstants.IS_APP_WAS_KILL = true;

        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(AppConstants.REQUEST_CODE_ASK_PERMISSIONS == 123) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    new PermissionMgr().requestPermission(this);
                    if (new PermissionMgr().checkPermission(this)) {
                        setUp();
                        hideFullScreenMessage();
                    }
                }
                return;
            }
        }
        if(AppConstants.PERMISSION_COUNT > 5)
            AppConstants.mainActivity.showMessage("Veuillez accorder les autorisations pour l'application dans les paramètres android");
        else AppConstants.PERMISSION_COUNT++;
    }
}