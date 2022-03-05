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

package net.rncmobile.fmdrivetest.data.db;

import android.util.Log;
import android.widget.Toast;

import net.rncmobile.fmdrivetest.data.db.model.CellRecorder;
import net.rncmobile.fmdrivetest.data.db.model.CellRecorderDao;
import net.rncmobile.fmdrivetest.data.db.model.DaoMaster;
import net.rncmobile.fmdrivetest.data.db.model.DaoSession;
import net.rncmobile.fmdrivetest.utils.AppConstants;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.core.Observable;


/**
 * Created by cedric_f25 25/12/17.
 */

@Singleton
public class AppDbHelper implements DbHelper {

    private DaoSession mDaoSession;

    @Inject
    public AppDbHelper(DbOpenHelper dbOpenHelper) {
        try {
            mDaoSession = new DaoMaster(dbOpenHelper.getWritableDb()).newSession();
        } catch (Exception e) {
            Toast.makeText(AppConstants.mainActivity, "Impossible d'ouvrir la base de données, merci de réinstaller l'application.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public long addCellRecorder(CellRecorder cellRecorder) {
        return mDaoSession.getCellRecorderDao().insert(cellRecorder);
    }

    @Override
    public Observable<Long> getNbCellRecorder() {
        return Observable.fromCallable((Callable<Long>) () -> {
            QueryBuilder<CellRecorder> qb = mDaoSession.getCellRecorderDao().queryBuilder();
            return qb.count();
        });
    }

}
