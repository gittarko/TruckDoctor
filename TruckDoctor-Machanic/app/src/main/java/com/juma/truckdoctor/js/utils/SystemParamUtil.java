package com.juma.truckdoctor.js.utils;

import android.content.Context;

import com.lhl.basetools.helper.MetaDataHelper;


/**
 * Created by Administrator on 2016/6/6 0006.
 */
public final class SystemParamUtil {

    private static final String KEY_ENV = "env";
    private static final String KEY_APP = "app";

    public static final String ENV_DEV = "dev";
    public static final String ENV_TEST = "test";
    public static final String ENV_OFFICIAL = "official";

    public static final String APP_HZ = "hz";
    public static final String APP_SJ = "sj";

    private static String paramEnv;
    private static String paramApp;

    public static void init(Context context) {
        paramEnv = MetaDataHelper.getMetaData(context, KEY_ENV);
        paramApp = MetaDataHelper.getMetaData(context, KEY_APP);
    }

    public static String getParamEnv() {
        return paramEnv;
    }

    public static String getParamApp() {
        return paramApp;
    }
}
