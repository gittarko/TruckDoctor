package com.juma.truckdoctor.js.api;

import android.text.TextUtils;

import com.juma.truckdoctor.js.utils.SystemParamUtil;

/**
 * Created by Administrator on 2016/8/4 0004.
 */

public class Api {
    public static final String PAY_HOST_DEV = "http://10.101.0.105:8081/";
    public static final String PAY_HOST_TEST = "http://10.101.0.16:8002/";
    public static final String PAY_HOST_OFFICIAL = "http://wallet.jumapeisong.com/";

    public static final String HOST_DEV = "http://10.101.0.105:8080/";
    public static final String HOST_TEST = "http://10.101.0.16:8001/";
    public static final String HOST_OFFICIAL = "http://api.lovedriver.cn/";

    public static final String URL_DRIVER_DEV = HOST_DEV + "forward/driver/loading.html";
    public static final String URL_DRIVER_TEST = HOST_TEST + "forward/driver/loading.html";
    public static final String URL_DRIVER_OFFICIAL = HOST_OFFICIAL + "forward/driver/loading.html";

    public static final String URL_MACHANIC_DEV = HOST_DEV + "forward/customer/loading.html";
    public static final String URL_MACHNIC_TEST = HOST_TEST + "forward/customer/loading.html";
    public static final String URL_MACHNIC_OFFICIAL = HOST_OFFICIAL + "forward/customer/loading.html";

    //客服联系电话
    public static final String Customer_Service_Phone = "4009922107";
    private static String host;
    private static String payHost;
    private static String urlDriver;
    private static String urlMachanic;
    private static String url;

    public static String getHost() {
        return host;
    }

    public static String getPayHost() {
        return payHost;
    }

    public static String getUrl() {
        return url;
    }

    public static void init() {
        if (TextUtils.equals(SystemParamUtil.ENV_TEST, SystemParamUtil.getParamEnv())) {
            host = HOST_TEST;
            payHost = PAY_HOST_TEST;
            urlDriver = URL_DRIVER_TEST;
            urlMachanic = URL_MACHNIC_TEST;
        } else if (TextUtils.equals(SystemParamUtil.ENV_OFFICIAL, SystemParamUtil.getParamEnv())) {
            host = HOST_OFFICIAL;
            payHost = PAY_HOST_OFFICIAL;
            urlDriver = URL_DRIVER_OFFICIAL;
            urlMachanic = URL_MACHNIC_OFFICIAL;
        } else if (TextUtils.equals(SystemParamUtil.ENV_DEV, SystemParamUtil.getParamEnv())) {
            host = HOST_DEV;
            payHost = PAY_HOST_DEV;
            urlDriver = URL_DRIVER_DEV;
            urlMachanic = URL_MACHANIC_DEV;
        }

        if (TextUtils.equals(SystemParamUtil.APP_DRIVER, SystemParamUtil.getParamApp())) {
            url = urlDriver;
        } else if (TextUtils.equals(SystemParamUtil.APP_MACHANIC, SystemParamUtil.getParamApp())) {
            url = urlMachanic;
        }

    }

}
