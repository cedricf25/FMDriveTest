package net.rncmobile.fmdrivetest.ui.settings;

import net.rncmobile.fmdrivetest.data.DataManager;
import net.rncmobile.fmdrivetest.managers.RadioManager;
import net.rncmobile.fmdrivetest.ui.base.BasePresenter;
import net.rncmobile.fmdrivetest.utils.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class SettingsPresenter <V extends SettingsMvpView> extends BasePresenter<V>
        implements SettingsMvpPresenter<V> {
    //private static final String TAG = "HuntingPresenter";

    @Inject
    SettingsPresenter(DataManager dataManager,
                     RadioManager radioManager,
                     SchedulerProvider schedulerProvider,
                     CompositeDisposable compositeDisposable) {
        super(dataManager, radioManager, schedulerProvider, compositeDisposable);
    }

    @Override
    public void onViewPrepared() {
    }
}
