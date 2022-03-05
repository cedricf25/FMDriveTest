package net.rncmobile.fmdrivetest.ui.base;

/**
 * Created by cedric_f25 25/12/17.
 */

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import net.rncmobile.fmdrivetest.data.DataManager;
import net.rncmobile.fmdrivetest.managers.RadioManager;
import net.rncmobile.fmdrivetest.utils.rx.SchedulerProvider;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class BasePresenter<V extends MvpView> implements MvpPresenter<V> {

    private static final String TAG = "BasePresenter";

    private final DataManager mDataManager;
    private final RadioManager mRadioManager;
    private final SchedulerProvider mSchedulerProvider;
    private final CompositeDisposable mCompositeDisposable;

    private V mMvpView;

    @Inject
    public BasePresenter(DataManager dataManager,
                         RadioManager radioManager,
                         SchedulerProvider schedulerProvider,
                         CompositeDisposable compositeDisposable) {
        this.mDataManager = dataManager;
        this.mRadioManager = radioManager;
        this.mSchedulerProvider = schedulerProvider;
        this.mCompositeDisposable = compositeDisposable;
    }

    @Override
    public void onAttach(V mvpView) {
        mMvpView = mvpView;
    }

    @Override
    public void onDetach() {
        mMvpView = null;
        mCompositeDisposable.clear();
    }

    @Override
    public void onDestroy() {
        mMvpView = null;
        //mCompositeDisposable.dispose();
    }

    @Override
    public void onDestroyView() {
        onDestroy();
    }

    public boolean isViewAttached() {
        return mMvpView != null;
    }

    public V getMvpView() {
        return mMvpView;
    }

    public void checkViewAttached() {
        if (!isViewAttached()) throw new MvpViewNotAttachedException();
    }

    public DataManager getDataManager() {
        return mDataManager;
    }

    public RadioManager getRadioManager() {
        return mRadioManager;
    }

    public SchedulerProvider getSchedulerProvider() {
        return mSchedulerProvider;
    }

    public CompositeDisposable getCompositeDisposable() {
        return mCompositeDisposable;
    }

    public static class MvpViewNotAttachedException extends RuntimeException {
        public MvpViewNotAttachedException() {
            super("Please call Presenter.onAttach(MainView) before" +
                    " requesting data to the Presenter");
        }
    }
}
