package net.rncmobile.fmdrivetest.data.db;

import android.content.Context;
import android.util.Log;

import net.rncmobile.fmdrivetest.data.db.model.DaoMaster;
import net.rncmobile.fmdrivetest.di.ApplicationContext;
import net.rncmobile.fmdrivetest.di.DatabaseInfo;

import org.greenrobot.greendao.database.Database;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by cedric_f25 on 25/12/17.
 */

@Singleton
public class DbOpenHelper extends DaoMaster.OpenHelper {
    private static final String TAG = "DbOpenHelper";

    @Inject
    public DbOpenHelper(@ApplicationContext Context context, @DatabaseInfo String name) {
        super(context, name);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
        Log.d(TAG, "Database want to upgrade from " + oldVersion + " to " + newVersion);
        switch (oldVersion) {
            case 1:
                Log.d(TAG, "Upgrading database to 1...");

        }
    }
}
