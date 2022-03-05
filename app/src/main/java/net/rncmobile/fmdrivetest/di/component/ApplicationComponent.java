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

package net.rncmobile.fmdrivetest.di.component;

import android.app.Application;
import android.content.Context;

import net.rncmobile.fmdrivetest.FMDApp;
import net.rncmobile.fmdrivetest.data.DataManager;
import net.rncmobile.fmdrivetest.di.ApiInfo;
import net.rncmobile.fmdrivetest.di.ApplicationContext;
import net.rncmobile.fmdrivetest.di.VersionInfo;
import net.rncmobile.fmdrivetest.di.module.ApplicationModule;
import net.rncmobile.fmdrivetest.managers.CellRecorderManager;
import net.rncmobile.fmdrivetest.managers.RadioManager;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by cedric_f25 on 27/01/17.
 */

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(FMDApp fmdApp);

    @ApplicationContext
    Context context();

    Application application();

    DataManager getDataManager();

    RadioManager getRadioManager();

    CellRecorderManager getCRManager();

    @ApiInfo
    String apiInfo();

    @VersionInfo
    String versionInfo();
}