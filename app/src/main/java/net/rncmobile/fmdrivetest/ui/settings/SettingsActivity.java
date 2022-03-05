package net.rncmobile.fmdrivetest.ui.settings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import net.rncmobile.fmdrivetest.R;
import net.rncmobile.fmdrivetest.databinding.ActivitySettingsBinding;
import net.rncmobile.fmdrivetest.ui.base.BaseActivity;

import javax.inject.Inject;

public class SettingsActivity extends BaseActivity implements SettingsMvpView {
    private static final String TAG = "SettingsActivity";

    @Inject
    SettingsPresenter<SettingsMvpView> mPresenter;

    private ActivitySettingsBinding binding;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, SettingsActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        getActivityComponent().inject(this);

        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mPresenter.onAttach(this);

        setUp();
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void setUp() {
        setSupportActionBar(binding.toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        getSupportFragmentManager().beginTransaction()
                .addToBackStack("preference_fragment")
                .add(R.id.fragment_container, SettingsFragment.newInstance()).commit();

        mPresenter.onViewPrepared();
    }

}
