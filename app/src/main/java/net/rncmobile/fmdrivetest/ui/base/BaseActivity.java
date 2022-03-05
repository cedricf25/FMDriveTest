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

package net.rncmobile.fmdrivetest.ui.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import net.rncmobile.fmdrivetest.FMDApp;
import net.rncmobile.fmdrivetest.R;
import net.rncmobile.fmdrivetest.di.component.ActivityComponent;
import net.rncmobile.fmdrivetest.di.component.DaggerActivityComponent;
import net.rncmobile.fmdrivetest.di.module.ActivityModule;
import net.rncmobile.fmdrivetest.utils.Utils;

/**
 * Created by cedric_f25 25/12/17.
 */

public abstract class BaseActivity extends AppCompatActivity
        implements MvpView, BaseFragment.Callback {

    private ProgressDialog mProgressDialog;

    private ActivityComponent mActivityComponent;

    public static final int snackPriorityInfo = 0;
    public static final int snackPriorityWarning = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityComponent = DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(((FMDApp) getApplication()).getComponent())
                .build();
    }

    public ActivityComponent getActivityComponent() {
        return mActivityComponent;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }

    @Override
    public void showLoading() {
        hideLoading();
        mProgressDialog = Utils.showLoadingDialog(this);
    }

    @Override
    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }

    private void showSnackBar(String message) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                message, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        //TextView textView = sbView.findViewById(R.id.snackbar_text);
        //textView.setTextColor(getResources().getColor(R.color.ColorWhite, getTheme()));
        snackbar.show();
    }

    public void showSnackBar(int priority, String message, String messageAction, View.OnClickListener onClickListener) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message, 5000);

        snackbar.setAction(messageAction, onClickListener);

        View sbView = snackbar.getView();
        //TextView textView = sbView.findViewById(R.id.snackbar_text);
        //TextView textActionView = sbView.findViewById(R.id.snackbar_action);

        sbView.setBackgroundColor(getResources().getColor(R.color.grey_10, getTheme()));
        //textView.setTextColor(getResources().getColor(R.color.ColorBlack, getTheme()));

        snackbar.setActionTextColor(getResources().getColor(R.color.grey_50, getTheme()));

        if(priority == snackPriorityWarning) {
            sbView.setBackgroundColor(getResources().getColor(R.color.ColorOrange, getTheme()));
            //textView.setTextColor(getResources().getColor(R.color.ColorBlack, getTheme()));
        }

        snackbar.show();
    }

    @Override
    public void onError(String message) {
        if (message != null) {
            showSnackBar(message);
        } else {
            showSnackBar("Erreur");
        }
    }

    @Override
    public void onError(@StringRes int resId) {
        onError(getString(resId));
    }

    @Override
    public void showMessage(String message) {
        if (message != null) Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        else Toast.makeText(this, "Erreur", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMessage(@StringRes int resId) {
        showMessage(getString(resId));
    }

    @Override
    public void startActivity(Intent intent, Uri uri) { }

    @Override
    public void onFragmentAttached() { }

    @Override
    public void onFragmentDetached(String tag) { }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            if(imm != null)
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected abstract void setUp();

    @Override
    public Context getContext() {
        return getBaseContext();
    }
}
