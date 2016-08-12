package com.juma.truckdoctor.js.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
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
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

import com.juma.truckdoctor.js.R;
import com.juma.truckdoctor.js.base.BaseActivity;
import com.juma.truckdoctor.js.base.BaseApplication;
import com.juma.truckdoctor.js.common.Constants;
import com.juma.truckdoctor.js.fragment.BackHandledFragment;
import com.juma.truckdoctor.js.fragment.BackHandledInterface;
import com.juma.truckdoctor.js.fragment.BaseWebFragment;
import com.juma.truckdoctor.js.fragment.OnCurrentFragmentCompleteListener;
import com.juma.truckdoctor.js.helper.CheckUpdateHelper;
import com.juma.truckdoctor.js.manager.PermissionManager;
import com.juma.truckdoctor.js.utils.EncryptUtils;
import com.juma.truckdoctor.js.utils.SystemParamUtil;
import com.juma.truckdoctor.js.widget.BadgeView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by hedong on 16/8/5.
 * 应用主页
 */

public class MainWebActivity extends BaseActivity implements BackHandledInterface, OnCurrentFragmentCompleteListener{
    private static final String TAG = MainWebActivity.class.getSimpleName();

    //订单提醒控件
    private BadgeView mBvOrders;

    private FragmentTabHost mTabHost;
    private BackHandledFragment backHandledFragment;
    private BaseWebFragment baseWebFragment;

    private PermissionManager permissionManager;
    private BroadcastReceiver mReceiver;

    //各个模块对应的入口地址
    private String[] indexUrls = null;
    private String url = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        baseWebFragment = BaseWebFragment.newInstance(url);
        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        if (android.os.Build.VERSION.SDK_INT > 10) {
            mTabHost.getTabWidget().setShowDividers(0);
        }

        initIndexUrl();
        initTabHost();
        start();

    }

    /**
     * 初始化每个模块对应的入口地址
     */
    private void initIndexUrl() {
        String env = SystemParamUtil.getParamEnv();
        String app = SystemParamUtil.getParamApp();
        String url_name = app + "_" + env;
        int array_id = getResources().getIdentifier(url_name, "array", getPackageName());
        if (array_id != 0) {
            indexUrls = getResources().getStringArray(array_id);
            for (int i = 1; i < indexUrls.length; i++) {
                indexUrls[i] = indexUrls[0] + indexUrls[i];
            }
        } else {
            Log.e(TAG, "identifier name:" + url_name + " is not exist!");
        }
    }

    /**
     * 初始化tabHost
     */
    private void initTabHost() {
        BottomTab[] tabs = BottomTab.values();
        for (int i = 0; i < tabs.length; i++) {
            BottomTab tab = tabs[i];
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(getResources().getString(tab.getTabName()));
            View indicator = getIndicatorView(tab);
            tabSpec.setIndicator(indicator);
            //将url地址传递到每个fragment
            Bundle data = new Bundle();
            data.putString(BaseWebFragment.ARG_PARAM1, indexUrls[i + 1]);
            mTabHost.addTab(tabSpec, tab.getClz(), data);

            if (tab.equals(BottomTab.ORDERS)) {
                View mes = indicator.findViewById(R.id.tab_mes);
                mBvOrders = new BadgeView(this, mes);
                //提醒放置在右上角
                mBvOrders.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
                //设置提醒的文字大小
                mBvOrders.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                //设置提醒背景
                mBvOrders.setBackgroundResource(R.drawable.notification_bg);
                mBvOrders.setGravity(Gravity.CENTER);
            }
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
        //请求应用权限
        permissionManager = new PermissionManager(this);
        permissionManager.checkAndRequestPermissions(PermissionManager.REQUEST_CODES_ALL_PERMISSON);
        //绑定极光推送
        BaseApplication.bindJpush();
        //检查应用更新
        CheckUpdateHelper.checkUpdate(this, permissionManager);
    }

    /**
     * 设置底部数字提醒
     * @param number
     */
    public void setTabNotice(int number) {
        if(number == 0) {
            mBvOrders.setText("");
            mBvOrders.hide();
        }else {
            mBvOrders.setText(String.valueOf(number));
            mBvOrders.show();
        }
    }

    @Override
    public void setSelectedFragment(BackHandledFragment selectedFragment) {
        this.backHandledFragment= selectedFragment;
    }

    @Override
    public void onCurrentFragmentComplete(Fragment fragment) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mTabHost.getCurrentTabTag().equals(getResources().getString(R.string.tab_my_order))) {
            //请求我的未处理订单数
            //每次应用由不可见到可见状态时都刷新提示数字
            Intent intent = new Intent();
            intent.setAction(Constants.INTENT_ACTION_NOTICE_REQUEST);
            sendBroadcast(intent);
        }
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
        permissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
