package com.juma.truckdoctor.js.utils;

import android.content.Context;

/**
 * Created by hedong on 16/8/4.
 *
 * APP常用操作工具类
 */

public class AppUtils {
    private static final String TAG = AppUtils.class.getSimpleName();

    /**
     * 获取屏幕宽度
     * @param context
     * @return  宽度
     */
    public static int getWindowWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @param context
     * @return
     */

    public static int getWindowHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

}
