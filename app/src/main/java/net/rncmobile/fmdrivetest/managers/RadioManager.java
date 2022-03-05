package net.rncmobile.fmdrivetest.managers;

import net.rncmobile.fmdrivetest.models.cells.IMyCell;

import java.util.List;

import io.reactivex.rxjava3.subjects.PublishSubject;

/**
 * Created by cedric_f25 on 27.12.2017.
 */

public interface RadioManager {
    void refreshSignalChangeObservers();
    void refreshCellChangeObservers();
    PublishSubject<IMyCell> registerOnSignalChange();
    PublishSubject<IMyCell> registerOnCellChange();

    void refreshApi();
    void stopApi();

    IMyCell getCurrentCell();
    List<IMyCell> getnCells();

    void setCrEnabled(boolean crEnabled);

    void debugCellChange();
}
