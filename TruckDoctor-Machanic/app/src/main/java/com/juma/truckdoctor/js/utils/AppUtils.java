package com.juma.truckdoctor.js.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.juma.truckdoctor.js.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    /**
     * 拨打电话
     */
    public static void getPhoneCall(Context context, String phoneNum) {
        if(isPhoneValid(phoneNum)) {
            Intent intent=new Intent("android.intent.action.CALL", Uri.parse("tel:"+phoneNum));
            context.startActivity(intent);
        }else{
            Toast.makeText(context, context.getResources().getString(R.string.error_format_phone),
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 检查是否是手机号格式
     * @param phoneNum 手机号码
     */
    public static boolean isPhoneValid(String phoneNum) {
        Pattern p = Pattern.compile("\\d+?");
        Matcher match = p.matcher(phoneNum);
        //正则验证输入的是否为数字
        if(match.matches()){
            return true;
        }

        return false;
    }

}
