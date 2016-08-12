package com.juma.truckdoctor.js.fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.juma.truckdoctor.js.activity.MainWebActivity;
import com.juma.truckdoctor.js.api.ApiResponse;
import com.juma.truckdoctor.js.api.ApiWeb;
import com.juma.truckdoctor.js.webview.JsInterface;

/**
 * Created by Administrator on 2016/8/10 0010.
 *
 * 订单列表Fragment
 */

public class OrderListWebFragment extends BaseWebFragment {
    private static final String TAG = OrderListWebFragment.class.getSimpleName();

    @Override
    protected View runNewInstance(LayoutInflater inflater, ViewGroup container) {
        View content = super.runNewInstance(inflater, container);
        //首次加载时请求订单未处理数字
        requestNoticeData();
        return content;
    }


    /**
     * 向WebView添加Java对象,供JS与JAVA交互
     * @param objectName
     * @return
     */
    @Override
    public JsInterface addJavaScriptInterface(String objectName) {
        JsInterface jsInterface = new JsInterface(getActivity(), webView);
        /*
         * 默认使用BaseWebFragment的JS_INTERFACE_NAME
         * 可以替换属于自己的对象名称
         */
        if(objectName == null) {
            objectName = JS_INTERFACE_NAME;
        }

        jsInterface.addJsInterface(objectName);
        return jsInterface;
    }

    @Override
    protected void requestNoticeData() {
        /**
         * 请求未处理订单提醒数字
         */
        ApiWeb.getUnprocessOrderNumber(new ApiResponse<Integer>() {
            @Override
            public void onSuccess(Integer response) {
                if(getActivity() instanceof MainWebActivity) {
                    ((MainWebActivity)getActivity()).setTabNotice(response);
                }
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, e.toString());
            }
        });
    }
}
