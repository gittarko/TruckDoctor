package com.juma.truckdoctor.js.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.KeyEvent;

import com.juma.truckdoctor.js.R;
import com.juma.truckdoctor.js.base.BaseActivity;
import com.juma.truckdoctor.js.fragment.BackHandledFragment;
import com.juma.truckdoctor.js.fragment.BackHandledInterface;
import com.juma.truckdoctor.js.fragment.BaseWebFragment;
import com.juma.truckdoctor.js.fragment.OnCurrentFragmentCompleteListener;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by hedong on 16/8/5.
 * 应用主页
 */

public class MainWebActivity extends BaseActivity implements BackHandledInterface, OnCurrentFragmentCompleteListener{
    private static final String TAG = MainWebActivity.class.getSimpleName();

    private String url = null;
    private BackHandledFragment backHandledFragment;
    private BaseWebFragment baseWebFragment;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseWebFragment = BaseWebFragment.newInstance(url);

        start();
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

    private void start() {
        FragmentManager fm = getSupportFragmentManager();
        //使用视频loading页
//        fm.beginTransaction().add(R.id.content, videoAnimationFragment).commitAllowingStateLoss();
        //使用普通loading页
        fm.beginTransaction().replace(R.id.content, baseWebFragment).commitAllowingStateLoss();
    }

    @Override
    public void setSelectedFragment(BackHandledFragment selectedFragment) {
        this.backHandledFragment= selectedFragment;
    }

    @Override
    public void onCurrentFragmentComplete(Fragment fragment) {

    }

    @Override
    public boolean onKeyDown(int keyCode, final KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (backHandledFragment.onBackPressed()) {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        baseWebFragment.onActivityRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (baseWebFragment != null) {
            try {
                baseWebFragment.getWebChromeClient().onActivityResult(requestCode, resultCode, data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
