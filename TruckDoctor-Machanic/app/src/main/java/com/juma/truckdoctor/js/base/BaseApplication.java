package com.juma.truckdoctor.js.base;

import android.app.Application;

import com.juma.truckdoctor.js.api.Api;
import com.juma.truckdoctor.js.exception.CrashHandler;
import com.juma.truckdoctor.js.utils.SystemParamUtil;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by dong.he on 2016/8/4 0004.
 *
 * 全局应用程序设置
 */

public class BaseApplication extends Application {
    //应用数据存储目录
    public static final String STORAGE_DIR;
    public static final String CRASH_DIR;
    //本地存储用户信息使用的标识key
    public static final String STORAGE_USER_KEY;
    static {
        STORAGE_DIR = "truckdoctor/";
        CRASH_DIR = STORAGE_DIR + "crash" + "/";
        STORAGE_USER_KEY = "truckdoctor_user";
    }

    public BaseApplication() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化全局异常捕获
        CrashHandler.getInstance().init(this)
                .setCacheDir(CRASH_DIR);
        SystemParamUtil.init(this);
        Api.init();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
