package net.rncmobile.fmdrivetest.di.module;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import net.rncmobile.fmdrivetest.di.ActivityContext;
import net.rncmobile.fmdrivetest.di.PerActivity;
import net.rncmobile.fmdrivetest.ui.main.MainMvpPresenter;
import net.rncmobile.fmdrivetest.ui.main.MainMvpView;
import net.rncmobile.fmdrivetest.ui.main.MainPresenter;
import net.rncmobile.fmdrivetest.ui.settings.SettingsMvpPresenter;
import net.rncmobile.fmdrivetest.ui.settings.SettingsMvpView;
import net.rncmobile.fmdrivetest.ui.settings.SettingsPresenter;
import net.rncmobile.fmdrivetest.utils.rx.AppSchedulerProvider;
import net.rncmobile.fmdrivetest.utils.rx.SchedulerProvider;
import java.util.ArrayList;

import dagger.Module;
import dagger.Provides;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

/**
 * Created by cedric_f25 25/12/17.
 */

@Module
public class ActivityModule {

    private final AppCompatActivity mActivity;

    public ActivityModule(AppCompatActivity activity) {
        this.mActivity = activity;
    }

    @Provides
    @ActivityContext
    Context provideContext() {
        return mActivity;
    }

    @Provides
    AppCompatActivity provideActivity() {
        return mActivity;
    }

    @Provides
    CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }

    @Provides
    @PerActivity
    MainMvpPresenter<MainMvpView> provideMainPresenter(
            MainPresenter<MainMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    SettingsMvpPresenter<SettingsMvpView> provideSettingsPresenter(
            SettingsPresenter<SettingsMvpView> presenter) {
        return presenter;
    }

    @Provides
    SchedulerProvider provideSchedulerProvider() {
        return new AppSchedulerProvider();
    }

    @Provides
    LinearLayoutManager provideLinearLayoutManager(AppCompatActivity activity) {
        return new LinearLayoutManager(activity);
    }
}
