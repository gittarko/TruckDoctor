package com.juma.truckdoctor.js.base;

import android.app.Application;
import android.content.Context;

import com.juma.truckdoctor.js.api.Api;
import com.juma.truckdoctor.js.exception.CrashHandler;
import com.juma.truckdoctor.js.utils.SystemParamUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;
import okhttp3.OkHttpClient;

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

    private static BaseApplication application;

    public static Context getContext() {
        return application.getApplicationContext();
    }

    public BaseApplication() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        //初始化全局异常捕获
        CrashHandler.getInstance().init(this)
                .setCacheDir(CRASH_DIR);
        initHttp();

        SystemParamUtil.init(this);
        Api.init();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }

    //初始化Http
    private void initHttp() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
