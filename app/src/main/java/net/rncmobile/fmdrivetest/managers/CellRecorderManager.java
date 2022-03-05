package net.rncmobile.fmdrivetest.managers;

import io.reactivex.rxjava3.subjects.PublishSubject;

public interface CellRecorderManager {
    PublishSubject<Boolean> registerOnCRInserted();

    void getCRStatus();

    boolean isCrEnabled();

    void startCellRecorder();
    void stopCellRecorder();
}
