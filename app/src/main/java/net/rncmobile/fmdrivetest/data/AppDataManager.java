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

package net.rncmobile.fmdrivetest.data;


import android.content.Context;

import net.rncmobile.fmdrivetest.data.db.DbHelper;
import net.rncmobile.fmdrivetest.data.db.model.CellRecorder;
import net.rncmobile.fmdrivetest.data.network.ApiHelper;
import net.rncmobile.fmdrivetest.data.prefs.PreferencesHelper;
import net.rncmobile.fmdrivetest.di.ApplicationContext;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by cedric_f25 25/12/17.
 */

@Singleton
public class AppDataManager implements DataManager {

    //private static final String TAG = "AppDataManager";

    private final Context mContext;
    private final DbHelper mDbHelper;
    private final PreferencesHelper mPreferencesHelper;
    private final ApiHelper mApiHelper;

    @Inject
    AppDataManager(@ApplicationContext Context context,
                          DbHelper dbHelper,
                          PreferencesHelper preferencesHelper,
                          ApiHelper apiHelper) {
        mContext = context;
        mDbHelper = dbHelper;
        mPreferencesHelper = preferencesHelper;
        mApiHelper = apiHelper;
    }

    //
    // DATABASE
    //
    @Override
    public long addCellRecorder(CellRecorder cellRecorder) {
        return mDbHelper.addCellRecorder(cellRecorder);
    }

    //
    // Preference
    //
    @Override
    public int getActiveSim() {
        return mPreferencesHelper.getActiveSim();
    }

    @Override
    public void setActiveSim(String activeSim) {
        mPreferencesHelper.setActiveSim(activeSim);
    }

    @Override
    public boolean isSartupSimChoice() {
        return mPreferencesHelper.isSartupSimChoice();
    }

    @Override
    public void setSartupSimChoice(boolean bool) {
        mPreferencesHelper.setSartupSimChoice(bool);
    }

    @Override
    public boolean isScreen() {
        return mPreferencesHelper.isScreen();
    }

    @Override
    public void setScreen(boolean screen) {
        mPreferencesHelper.setScreen(screen);
    }

    @Override
    public String getServeur() {
        return mPreferencesHelper.getServeur();
    }

    @Override
    public void setServeur(String serveur) {
        mPreferencesHelper.setServeur(serveur);
    }
}
