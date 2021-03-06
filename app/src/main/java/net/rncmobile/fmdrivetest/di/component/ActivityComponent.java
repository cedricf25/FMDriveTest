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

import net.rncmobile.fmdrivetest.ui.main.MainActivity;
import net.rncmobile.fmdrivetest.di.PerActivity;
import net.rncmobile.fmdrivetest.di.module.ActivityModule;
import net.rncmobile.fmdrivetest.ui.settings.SettingsActivity;

import dagger.Component;

/**
 * Created by cedric_25 on 25/12/17.
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(MainActivity activity);
    void inject(SettingsActivity activity);
}
