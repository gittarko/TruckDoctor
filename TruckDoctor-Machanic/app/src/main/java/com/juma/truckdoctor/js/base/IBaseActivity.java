package com.juma.truckdoctor.js.base;

import android.view.View;

/**
 * Created by hedong on 16/8/5.
 */

public interface IBaseActivity {

    void showToast(CharSequence text, int duration);

    void showToast(int resId, int duration);

    void showProgressDialog();

    void showProgressDialog(String msg, boolean cancelable);

    void dismissProgressDialog();
}
