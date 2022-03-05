package net.rncmobile.fmdrivetest.ui.settings;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.telephony.SubscriptionInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import net.rncmobile.fmdrivetest.R;
import net.rncmobile.fmdrivetest.data.prefs.AppPreferencesHelper;
import net.rncmobile.fmdrivetest.data.prefs.PreferencesHelper;
import net.rncmobile.fmdrivetest.utils.AppConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cz.mroczis.netmonster.core.factory.NetMonsterFactory;
import cz.mroczis.netmonster.core.model.SubscribedNetwork;

/**
 * Created by cedricf_25 on 12/10/2015.
 */
public class SettingsFragment extends PreferenceFragmentCompat {
    //private static final String TAG = "SettingsFragment";

    private EditTextPreference serverTxt;
    private ListPreference listPreference;
    private ListPreference listNbSim;

    private PreferencesHelper mPrefs;

    List<SubscriptionInfo> subInfoList;

    public static SettingsFragment newInstance() {

        Bundle args = new Bundle();

        SettingsFragment fragment = new SettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getPreferenceManager().setSharedPreferencesName("myPref");
        addPreferencesFromResource(R.xml.preferences);

        mPrefs = new AppPreferencesHelper(requireActivity(), AppConstants.PREF_NAME);
    }

    @SuppressLint("MissingPermission")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = super.onCreateView(inflater, container, savedInstanceState);

        serverTxt = findPreference("serveur");
        listNbSim = findPreference("nb_sim");

        serverTxt.setOnPreferenceChangeListener((Preference preference, Object o) -> {
            if (o.toString().length() < 3) {
                Toast.makeText(getActivity(), "Non autorisé ou inférieur à 3 lettres", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                serverTxt.setSummary("Nouveau serveur : " + o.toString());
                mPrefs.setServeur(o.toString());
            }
            return true;
        });

        listNbSim.setOnPreferenceChangeListener((Preference preference, Object o) -> {
            listNbSim.setSummary("Sim " + o.toString() + " activée");
            mPrefs.setActiveSim(o.toString());
            return true;
        });

        // SIM Managment

        List<String> listEntries = new ArrayList<>();
        List<String> listEntriesValue = new ArrayList<>();

        try {
            List<SubscribedNetwork> subInfoList = NetMonsterFactory.INSTANCE.getSubscription(requireContext()).getActiveSubscriptions();
            if (subInfoList.size() > 0) {
                for (int i = 0; i < subInfoList.size(); i++) {

                    String mcc = "00";
                    String mnc = "00";
                    if (subInfoList.get(i).getNetwork() != null) {
                        mcc = Objects.requireNonNull(subInfoList.get(i).getNetwork()).getMcc();
                        mnc = Objects.requireNonNull(subInfoList.get(i).getNetwork()).getMnc();
                    }
                    int subID = subInfoList.get(i).getSubscriptionId();

                    listEntries.add("SIM " + subID + " (" + mcc + mnc + ")");
                    listEntriesValue.add(String.valueOf(subID));
                }
            } else {
                listEntries.add("Err1: Vérifiez les autorisations");
                listEntriesValue.add("-1");
            }
        } catch (Exception e) {
            listEntries.add("Err2: Vérifiez les autorisations de l'application dans les paramètres Android / " + e.toString());
            listEntriesValue.add("-1");
            listNbSim.setSummary("Err3: Vérifiez les autorisations");
            Toast.makeText(this.requireContext(), "Err2: " + e.toString(), Toast.LENGTH_LONG).show();
        }

        listNbSim.setEntries(listEntries.toArray(new CharSequence[0]));
        listNbSim.setEntryValues(listEntriesValue.toArray(new CharSequence[0]));
        if (listNbSim.getValue().equals("-1"))
            listNbSim.setSummary("Err4: Vérifiez les autorisations");
        else
            listNbSim.setSummary("Sim " + listNbSim.getValue() + " activée");


        return view;
    }
}
