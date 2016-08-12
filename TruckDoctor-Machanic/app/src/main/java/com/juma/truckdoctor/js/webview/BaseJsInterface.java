package com.juma.truckdoctor.js.webview;

/**
 * Created by hedong on 16/8/11.
 */

public interface BaseJsInterface {

    /**
     * 为WebView添加JAVA调用对象
     * @param objectName
     */
    void addJsInterface(String objectName);

    //返回
    void onBackPressd();

    /**
     * 监听键盘显示状态,通知WEB动态改变布局
     * @param change true代表键盘弹起 false代表键盘收回
     */
    void onInputChange(boolean change);

}
