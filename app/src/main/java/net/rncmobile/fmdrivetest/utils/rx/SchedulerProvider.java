package net.rncmobile.fmdrivetest.utils.rx;


import io.reactivex.rxjava3.core.Scheduler;

/**
 * Created by cedric_f25 on 25/12/17.
 */

public interface SchedulerProvider {
    Scheduler ui();

    Scheduler computation();

    Scheduler io();
}
