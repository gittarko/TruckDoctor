package com.juma.truckdoctor.js.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.juma.truckdoctor.js.activity.login.LoginActivity;
import com.juma.truckdoctor.js.api.Api;
import com.juma.truckdoctor.js.base.BaseActivity;
import com.juma.truckdoctor.js.base.BaseApplication;
import com.juma.truckdoctor.js.utils.CacheManager;

/**
 * Created by hedong on 16/8/4.
 * APP启动界面
 *
 */

public class LaunchActivity extends BaseActivity {
    @Override
    protected int getLayoutResId() {
        return 0;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkLoginUser();
    }


    /**
     * 检测是否存在已登录用户
     * 存在进入主页,不存在跳转登录
     */
    private void checkLoginUser() {
        String cacheUser = BaseApplication.STORAGE_USER_KEY;
        if(CacheManager.isExistDataCache(this, cacheUser)) {
            //存在登录用户
//            Intent homeIntent = new Intent(this,)
        }else {
            //跳转至登录
            Intent loginIntent = new Intent(this, LoginActivity.class);
            loginIntent.setData(Uri.parse(Api.getUrl()));
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(loginIntent);
            finish();
        }
    }

}
