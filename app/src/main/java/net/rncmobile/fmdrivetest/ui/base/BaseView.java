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
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import net.rncmobile.fmdrivetest.FMDApp;
import net.rncmobile.fmdrivetest.utils.Utils;

/**
 * Created by cedric_f25 25/12/17.
 */

public abstract class BaseView<V> extends RelativeLayout implements MvpView {

    private ProgressDialog mProgressDialog;

    public BaseView(Context context) {
        super(context);
        init();
    }

    public BaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {

    }


    @Override
    public void showLoading() {
        hideLoading();
        mProgressDialog = Utils.showLoadingDialog(getContext());
    }

    @Override
    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }

    @Override
    public void onError(int resId) {

    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void showMessage(int resId) {

    }

    @Override
    public void startActivity(Intent intent) {

    }

    @Override
    public void startActivity(Intent intent, Uri uri) {

    }

    @Override
    public void hideKeyboard() {

    }

    protected abstract void setUp();
}
