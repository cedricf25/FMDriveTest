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

package net.rncmobile.fmdrivetest.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import net.rncmobile.fmdrivetest.di.ApplicationContext;
import net.rncmobile.fmdrivetest.di.PreferenceInfo;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by cedric_f25 25/12/17.
 */

@Singleton
public class AppPreferencesHelper implements PreferencesHelper {

    private final SharedPreferences mPrefs;

    @Inject
    public AppPreferencesHelper(@ApplicationContext Context context,
                                @PreferenceInfo String prefFileName) {
        mPrefs = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
    }

    public int getActiveSim() {
        return Integer.valueOf(mPrefs.getString("nb_sim", "1"));
    }

    public void setActiveSim(String activeSim) {
        mPrefs.edit().putString("nb_sim", activeSim).apply();
    }
}
