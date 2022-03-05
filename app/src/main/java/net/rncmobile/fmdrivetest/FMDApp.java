package net.rncmobile.fmdrivetest;

import android.app.Application;

import net.rncmobile.fmdrivetest.di.component.ApplicationComponent;
import net.rncmobile.fmdrivetest.di.component.DaggerApplicationComponent;
import net.rncmobile.fmdrivetest.di.module.ApplicationModule;

public class FMDApp  extends Application {
    private static final String TAG = "FMDApp";

    private ApplicationComponent mApplicationComponent;

    public void onCreate(){
        super.onCreate();

        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this)).build();

        mApplicationComponent.inject(this);
    }

    public ApplicationComponent getComponent() {
        return mApplicationComponent;
    }
}