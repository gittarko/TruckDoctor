package com.juma.truckdoctor.js.base;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.juma.truckdoctor.js.api.Api;
import com.juma.truckdoctor.js.api.ApiUser;
import com.juma.truckdoctor.js.exception.CrashHandler;
import com.juma.truckdoctor.js.model.User;
import com.juma.truckdoctor.js.utils.SystemParamUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import okhttp3.OkHttpClient;

/**
 * Created by dong.he on 2016/8/4 0004.
 *
 * 全局应用程序设置
 */

public class BaseApplication extends Application {
    private static final String TAG = BaseApplication.class.getSimpleName();

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

    //是否保存全局Crash日志
    private boolean IS_SAVE_CRASH = false;

    //全局application单例
    private static BaseApplication application;
    //已登录用户信息
    private static User loginUser;

    public static User getUser() {
        if(loginUser == null) {
            loginUser = ApiUser.syncGetLocalUserData(getContext());
        }
        return loginUser;
    }

    public static Context getContext() {
        return application.getApplicationContext();
    }

    public BaseApplication() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        if(IS_SAVE_CRASH) {
            //初始化全局异常捕获
            CrashHandler.getInstance().init(this)
                    .setCacheDir(CRASH_DIR);
        }

        initHttp();
        SystemParamUtil.init(this);
        Api.init();
        initJpush();
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

    private void initJpush() {
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }

    /**
     * 绑定极光推送,需要登录用户信息
     * 使用用户userId绑定
     */
    public static void bindJpush() {
        User user = getUser();
        if(user == null) {
            Log.e(TAG, "绑定极光推送失败，缺失user信息");
            return;
        }

        /**
         * @param alias

         "" （空字符串）表示取消之前的设置。
         每次调用设置有效的别名，覆盖之前的设置。
         有效的别名组成：字母（区分大小写）、数字、下划线、汉字、特殊字符(v2.1.6支持)@!#$&*+=.|￥。
         限制：alias 命名长度限制为 40 字节。（判断长度需采用UTF-8编码）

         @param callback
         在TagAliasCallback 的 gotResult 方法，返回对应的参数 alias, tags。并返回对应的状态码：0为成功，其他返回码请参考错误码定义。

         */
        JPushInterface.setAlias(getContext(), String.valueOf(user.getUserId()), new TagAliasCallback() {
            @Override
            public void gotResult(int responseCode, String alias, Set<String> set) {
                if(responseCode == 0) {
                    Log.i(TAG, "极光推送绑定成功");
                }else {
                    Log.e(TAG, "极光推送绑定失败: error code " + responseCode);
                }
            }
        });
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
