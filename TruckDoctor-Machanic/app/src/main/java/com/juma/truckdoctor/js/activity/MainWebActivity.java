package com.juma.truckdoctor.js.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.juma.truckdoctor.js.R;
import com.juma.truckdoctor.js.base.BaseActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by hedong on 16/8/5.
 * 应用主页
 */

public class MainWebActivity extends BaseActivity {
    private static final String TAG = MainWebActivity.class.getSimpleName();

    private String url = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_web;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        processIntent(intent);

    }

    @Override
    public void initIntent() {
        super.initIntent();
        processIntent(getIntent());
    }

    private void processIntent(Intent intent) {
        url = getIntent().getDataString();
//        Uri uri = Uri.parse(url);
//        if (SCHEME.equals(uri.getScheme()) && HOST.equals(uri.getHost())) {
//            String uriString = uri.getQueryParameter(PARAM_TARGET_URL1);
//            if (TextUtils.isEmpty(uriString)) {
//                uriString = uri.getQueryParameter(PARAM_TARGET_URL2);
//            }
//            if (TextUtils.isEmpty(uriString)) {
//                uriString = uri.getQueryParameter(PARAM_TARGET_URL3);
//            }
//            if (TextUtils.isEmpty(uriString)) {
//                uriString = uri.getQueryParameter(PARAM_TARGET_URL4);
//            }
//            try {
//                url = URLDecoder.decode(uriString, "utf-8");
//            } catch (UnsupportedEncodingException e) {
//                url = uriString;
//            }
//        }
    }
}
