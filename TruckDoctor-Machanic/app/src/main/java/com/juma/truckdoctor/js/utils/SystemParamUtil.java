package com.juma.truckdoctor.js.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

/**
 * Created by Administrator on 2016/6/6 0006.
 */
public final class SystemParamUtil {

    private static final String KEY_ENV = "env";
    private static final String KEY_APP = "app";

    public static final String ENV_DEV = "dev";
    public static final String ENV_TEST = "test";
    public static final String ENV_OFFICIAL = "prod";

    public static final String APP_MACHANIC = "machanic";
    public static final String APP_DRIVER = "driver";

    private static String paramEnv;
    private static String paramApp;

    public static void init(Context context) {
        paramEnv = getMetaData(context, KEY_ENV);
        paramApp = getMetaData(context, KEY_APP);
    }

    public static String getMetaData(Context context, String name) {
        try {
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return appInfo.metaData.getString(name);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getParamEnv() {
        return paramEnv;
    }

    public static String getParamApp() {
        return paramApp;
    }
}
