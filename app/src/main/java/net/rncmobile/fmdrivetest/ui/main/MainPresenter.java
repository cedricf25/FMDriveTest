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

import android.content.Context;

import net.rncmobile.fmdrivetest.data.DataManager;
import net.rncmobile.fmdrivetest.di.ActivityContext;
import net.rncmobile.fmdrivetest.di.ApplicationContext;
import net.rncmobile.fmdrivetest.managers.CellRecorderManager;
import net.rncmobile.fmdrivetest.managers.RadioManager;
import net.rncmobile.fmdrivetest.ui.base.BasePresenter;
import net.rncmobile.fmdrivetest.utils.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.rxjava3.disposables.CompositeDisposable;


/**
 * Created by cedric_f25 on 25/11/17.
 */

public class MainPresenter<V extends MainMvpView> extends BasePresenter<V>
        implements MainMvpPresenter<V> {
    private static final String TAG = "MainPresenter";

    @Inject
    @ApplicationContext
    Context context;
    @Inject
    @ActivityContext
    Context activityContext;
    @Inject
    CellRecorderManager cellRecorderManager;

    @Inject
    MainPresenter(DataManager dataManager,
                  RadioManager radioManager,
                  SchedulerProvider schedulerProvider,
                  CompositeDisposable compositeDisposable) {
        super(dataManager, radioManager, schedulerProvider, compositeDisposable);
    }

    @Override
    public CellRecorderManager getCellRecorderManager() {
        return cellRecorderManager;
    }
}
