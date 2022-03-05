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
import android.util.Log;

import net.rncmobile.fmdrivetest.R;
import net.rncmobile.fmdrivetest.data.DataManager;
import net.rncmobile.fmdrivetest.di.ActivityContext;
import net.rncmobile.fmdrivetest.di.ApplicationContext;
import net.rncmobile.fmdrivetest.listeners.FusedLocationListener;
import net.rncmobile.fmdrivetest.managers.CellRecorderManager;
import net.rncmobile.fmdrivetest.managers.RadioManager;
import net.rncmobile.fmdrivetest.models.MyTelephonyFactory;
import net.rncmobile.fmdrivetest.models.cells.IMyCell;
import net.rncmobile.fmdrivetest.ui.base.BasePresenter;
import net.rncmobile.fmdrivetest.utils.rx.SchedulerProvider;

import java.util.List;

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
    public void onViewPrepared() {
        nbCollecte();
    }

    @Override
    public void registerOnCellChange() {
        getCompositeDisposable().add(getRadioManager()
                .registerOnCellChange()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe((IMyCell cell) -> {
                    if(isViewAttached()) {
                        if (MyTelephonyFactory.getInstance().get(context).isPlaneMode()) {
                            getMvpView().displayFullScreenMessage(null, R.drawable.ic_airplanemode_active_black_24dp,
                                    context.getString(R.string.planeModeTitle), context.getString(R.string.planeModeDesc));
                            return;
                        }
                        if (!(new FusedLocationListener(context).isGpsEnabled())) {
                            getMvpView().displayFullScreenMessage(null, R.drawable.ic_location_off_black_24dp,
                                    context.getString(R.string.noGpsTitle), context.getString(R.string.noGpsnDesc));
                            return;
                        }
                        if (cell.getLcid() <= 0) {
                            getMvpView().displayFullScreenMessage(null, R.drawable.ic_signal_cellular_off_black_24dp,
                                    context.getString(R.string.noSignalTitle), context.getString(R.string.noSignalDesc));

                        } else
                            getMvpView().hideFullScreenMessage();
                    }
                },(Throwable throwable) -> Log.d(TAG, throwable.toString())));
    }

    @Override
    public void registerOnSignalChange() {
        getCompositeDisposable().add(getRadioManager().registerOnSignalChange()
                .subscribeOn(getSchedulerProvider().io()).observeOn(getSchedulerProvider().ui())
                .subscribe((IMyCell cell) -> {
                    if(isViewAttached()) {
                        if (MyTelephonyFactory.getInstance().get(context).isPlaneMode()) {
                            getMvpView().planeMode();
                            return;
                        }
                        if (!(new FusedLocationListener(context).isGpsEnabled())) {
                            getMvpView().noGps();
                            return;
                        }
                        if (cell.getLcid() > 0) getMvpView().refreshMonitor(cell);
                        else getMvpView().noCell();
                    }
                },(Throwable throwable) -> Log.d(TAG, "OnSignalChange: " + throwable.toString() + "\n" + throwable.getMessage())));
    }

    @Override
    public void registerOnCellRecorderChange() {
        getCompositeDisposable().add(cellRecorderManager.registerOnCRInserted()
                .subscribeOn(getSchedulerProvider().io()).observeOn(getSchedulerProvider().ui())
                .subscribe((Boolean b) -> {
                    if(isViewAttached()) {
                        nbCollecte();
                        getMvpView().refreshInfos();
                    }
                },(Throwable throwable) -> Log.d(TAG, "OnSignalChange: " + throwable.toString() + "\n" + throwable.getMessage())));
    }

    @Override
    public Boolean prefIsScreen() {
        return getDataManager().isScreen();
    }

    @Override
    public CellRecorderManager getCellRecorderManager() {
        return cellRecorderManager;
    }

    @Override
    public Boolean prefIsSartupSimChoice() {
        return getDataManager().isSartupSimChoice();
    }

    @Override
    public void setIsSartupSimChoice(boolean isSartupSimChoice) {
        getDataManager().setSartupSimChoice(isSartupSimChoice);
    }

    @Override
    public void setActiveSimsetActiveSim(String activeSim) {
        getDataManager().setActiveSim(activeSim);
    }

    @Override
    public void nbCollecte() {
        getCompositeDisposable().add(getDataManager()
                .getNbCellRecorder()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe((Long cellRecorders) -> {
                    if(isViewAttached())
                        getMvpView().setNbCellRecorder(cellRecorders);
                }, (Throwable throwable) -> Log.d(TAG, "Erreur onClickDataExport: " + throwable.toString())));
    }
}
