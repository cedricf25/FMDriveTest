package net.rncmobile.fmdrivetest.data;

import net.rncmobile.fmdrivetest.data.db.DbHelper;
import net.rncmobile.fmdrivetest.data.network.ApiHelper;
import net.rncmobile.fmdrivetest.data.prefs.PreferencesHelper;

/**
 * Created by cedric_f25 25/12/17.
 */

public interface DataManager extends DbHelper, PreferencesHelper, ApiHelper {
}
