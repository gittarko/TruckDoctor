package com.juma.truckdoctor.js.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

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

    private FragmentTabHost mTabHost;

    private String url = null;
    private BackHandledFragment backHandledFragment;
    private BaseWebFragment baseWebFragment;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        baseWebFragment = BaseWebFragment.newInstance(url);
        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        if (android.os.Build.VERSION.SDK_INT > 10) {
            mTabHost.getTabWidget().setShowDividers(0);
        }

        initTabHost();
        start();
    }

    /**
     * 初始化tabHost
     */
    private void initTabHost() {
        BottomTab[] tabs = BottomTab.values();
        for(int i=0; i<tabs.length; i++) {
            BottomTab tab = tabs[i];
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(tab.getTabName());
            View indicator = getIndicatorView(tab);
            tabSpec.setIndicator(indicator);
            mTabHost.addTab(tabSpec, tab.getClz(), null);
        }
    }

    /**
     * 初始化与tabHost关联的视图
     * @param mainTab
     * @return
     */
    private View getIndicatorView(BottomTab mainTab) {
        //设置drawableTop
        View indicator = getLayoutInflater().from(getApplicationContext())
                .inflate(R.layout.tab_indicator, null);
        TextView title = (TextView) indicator.findViewById(R.id.tab_title);
        Drawable drawable = this.getResources().getDrawable(
                mainTab.getResIcon());
        //设置文字上部图标
        title.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null,
                null);
        //设置页卡文字内容
        title.setText(mainTab.getTabName());
        return indicator;
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
//        FragmentManager fm = getSupportFragmentManager();
        //使用视频loading页
//        fm.beginTransaction().add(R.id.content, videoAnimationFragment).commitAllowingStateLoss();
        //使用普通loading页
//        fm.beginTransaction().replace(R.id.content, baseWebFragment).commitAllowingStateLoss();
        mTabHost.setCurrentTab(0);

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
//        baseWebFragment.onActivityRequestPermissionsResult(requestCode, permissions, grantResults);
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
