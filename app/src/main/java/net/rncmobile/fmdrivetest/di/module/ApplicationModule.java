/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package net.rncmobile.fmdrivetest.di.module;

import android.app.Application;
import android.content.Context;

import net.rncmobile.fmdrivetest.BuildConfig;
import net.rncmobile.fmdrivetest.data.AppDataManager;
import net.rncmobile.fmdrivetest.data.DataManager;
import net.rncmobile.fmdrivetest.data.db.AppDbHelper;
import net.rncmobile.fmdrivetest.data.db.DbHelper;
import net.rncmobile.fmdrivetest.data.network.ApiHelper;
import net.rncmobile.fmdrivetest.data.network.AppApiHelper;
import net.rncmobile.fmdrivetest.data.prefs.AppPreferencesHelper;
import net.rncmobile.fmdrivetest.data.prefs.PreferencesHelper;
import net.rncmobile.fmdrivetest.di.ApiInfo;
import net.rncmobile.fmdrivetest.di.ApplicationContext;
import net.rncmobile.fmdrivetest.di.DatabaseInfo;
import net.rncmobile.fmdrivetest.di.PreferenceInfo;
import net.rncmobile.fmdrivetest.di.VersionInfo;
import net.rncmobile.fmdrivetest.managers.AppCellRecorderManager;
import net.rncmobile.fmdrivetest.managers.AppRadioManager;
import net.rncmobile.fmdrivetest.managers.CellRecorderManager;
import net.rncmobile.fmdrivetest.managers.RadioManager;
import net.rncmobile.fmdrivetest.utils.AppConstants;
import net.rncmobile.fmdrivetest.utils.rx.AppSchedulerProvider;
import net.rncmobile.fmdrivetest.utils.rx.SchedulerProvider;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by cedric_f25 on 25/11/17.
 */

@Module
public class ApplicationModule {

    private final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication.getApplicationContext();
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @DatabaseInfo
    String provideDatabaseName() {
        return AppConstants.DB_NAME;
    }

    @Provides
    @ApiInfo
    String provideApiKey() {
        return BuildConfig.API_KEY;
    }

    @Provides
    @VersionInfo
    String provideVersionInfo() {
        return BuildConfig.VERSION_NAME;
    }

    @Provides
    @PreferenceInfo
    String providePreferenceName() {
        return AppConstants.PREF_NAME;
    }

    @Provides
    @Singleton
    DataManager provideDataManager(AppDataManager appDataManager) {
        return appDataManager;
    }

    @Provides
    @Singleton
    SchedulerProvider SchedulerProvider(AppSchedulerProvider appSchedulerProvider) {
        return appSchedulerProvider;
    }

    @Provides
    @Singleton
    DbHelper provideDbHelper(AppDbHelper appDbHelper) {
        return appDbHelper;
    }

    @Provides
    @Singleton
    PreferencesHelper providePreferencesHelper(AppPreferencesHelper appPreferencesHelper) {
        return appPreferencesHelper;
    }

    @Provides
    @Singleton
    ApiHelper provideApiHelper(AppApiHelper appApiHelper) {
        return appApiHelper;
    }

    @Provides
    @Singleton
    RadioManager provideRadioHelper(AppRadioManager appRadioHelper) { return appRadioHelper; }

    @Provides
    @Singleton
    CellRecorderManager provideCRHelper(AppCellRecorderManager appCellRecorderManager) { return appCellRecorderManager; }

}