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

package net.rncmobile.fmdrivetest.ui.main;

import net.rncmobile.fmdrivetest.di.PerActivity;
import net.rncmobile.fmdrivetest.managers.CellRecorderManager;
import net.rncmobile.fmdrivetest.managers.RadioManager;
import net.rncmobile.fmdrivetest.ui.base.MvpPresenter;
import net.rncmobile.fmdrivetest.ui.base.MvpView;

/**
 * Created by cedric_f25 on 27/01/17.
 */

@PerActivity
public interface MainMvpPresenter<V extends MainMvpView> extends MvpPresenter<V> {
    void onViewPrepared();

    void registerOnCellChange();
    void registerOnSignalChange();
    void registerOnCellRecorderChange();

    Boolean prefIsScreen();

    RadioManager getRadioManager();
    CellRecorderManager getCellRecorderManager();

    Boolean prefIsSartupSimChoice();
    void setIsSartupSimChoice(boolean SartupSimChoice);
    void setActiveSimsetActiveSim(String activeSim);

    void nbCollecte();
}
