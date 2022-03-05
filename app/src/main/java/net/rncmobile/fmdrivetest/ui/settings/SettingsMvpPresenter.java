package net.rncmobile.fmdrivetest.ui.settings;

import net.rncmobile.fmdrivetest.ui.base.MvpPresenter;

public interface SettingsMvpPresenter<V extends SettingsMvpView> extends MvpPresenter<V> {
    void onViewPrepared();
}
