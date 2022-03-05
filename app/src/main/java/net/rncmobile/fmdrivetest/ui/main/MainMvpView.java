package net.rncmobile.fmdrivetest.ui.main;

import android.view.View;

import net.rncmobile.fmdrivetest.models.cells.IMyCell;
import net.rncmobile.fmdrivetest.ui.base.MvpView;

/**
 * Created by cedric_f25 on 25.12.2017.
 */


public interface MainMvpView extends MvpView {
    void displayFullScreenMessage(View.OnClickListener clickListener, int image, String title, String description);
    void hideFullScreenMessage();

    void noCell();
    void planeMode();
    void noGps();

    void refreshMonitor(IMyCell cell);

    void refreshInfos();

    void setNbCellRecorder(Long nbCellRecorder);
}
