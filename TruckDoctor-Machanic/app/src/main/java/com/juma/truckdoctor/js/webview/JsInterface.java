package com.juma.truckdoctor.js.webview;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;

import com.juma.truckdoctor.js.helper.CheckUpdateHelper;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by Administrator on 2016/5/25 0025.
 */
public class JsInterface implements BaseJsInterface{

    private static final String TAG = "JsInterface";

    private Context mContext;
    private WebView mWebView;
//    private WebChromeClient mWebChromeClient;
//    private String mUriString;
    private Handler mHandler;

    public JsInterface(Context context, WebView webView/*, WebChromeClient webChromeClient*/) {
        mContext = context;
        mWebView = webView;
//        mWebChromeClient = webChromeClient;
        mHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * java调用js
     * @param jsString
     */
    protected void execJavaScript(final String jsString) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    mWebView.loadUrl("javascript:" + jsString + ";");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showToast(final CharSequence text, final int duration) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mContext, text, duration).show();
            }
        });
    }

    private void showToast(final int resId, final int duration) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mContext, resId, duration).show();
            }
        });
    }

    private String payingTradeNo = null;

    public void updatePayWaybillStatusComplete() {
        if (!TextUtils.isEmpty(payingTradeNo)) {
//            WxPayHelper.updatePayWaybillStatusComplete(payingTradeNo);
        }
    }

    public void updatePayWaybillStatusOriginal() {
        if (!TextUtils.isEmpty(payingTradeNo)) {
//            WxPayHelper.updatePayWaybillStatusOriginal(payingTradeNo);
        }
    }

    @JavascriptInterface
    public void wxPay(String tradeNo, String fee, String body, String ip, String userId) {
        //判断是否安装微信
//        IWXAPI api = WXAPIFactory.createWXAPI(mContext, Constants.APP_ID, false);
//        api.registerApp(Constants.APP_ID);
//        if (!api.isWXAppInstalled()) {
//            showToast(R.string.wxpay_not_install, Toast.LENGTH_SHORT);
//            setWxPayResult("-1");
//            return;
//        }
//        if (!api.isWXAppSupportAPI()) {
//            showToast(R.string.wxpay_not_support, Toast.LENGTH_SHORT);
//            setWxPayResult("-1");
//            return;
//        }
//
//        payingTradeNo = tradeNo;
//        PayInfo payInfo = new PayInfo();
//        payInfo.body = body;
//        payInfo.out_trade_no = tradeNo;
//        payInfo.spbill_create_ip = ip;
//        payInfo.total_fee = fee;
//        payInfo.attach = userId;
//        payInfo.appKeyFlag = "1";
//
//        mHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                if (mContext instanceof IBaseActivity) {
//                    ((IBaseActivity) mContext).showProgressDialog();
//                }
//            }
//        });
//
//        WxPayHelper.wxPay(mContext, payInfo, new ResultCallback<Pair<Integer, String>>() {
//            @Override
//            public void onError(Call call, Exception e) {
//                if (mContext instanceof IBaseActivity) {
//                    ((IBaseActivity) mContext).dismissProgressDialog();
//                    ((IBaseActivity) mContext).showToast(R.string.get_prepay_fail, Toast.LENGTH_SHORT);
//                }
//            }
//
//            @Override
//            public void onResponse(Call call, Pair<Integer, String> response) {
//                if (mContext instanceof IBaseActivity) {
//                    ((IBaseActivity) mContext).dismissProgressDialog();
////                    ((IBaseActivity) mContext).showToast("code:" + response.first + "|msg:" + response.second, Toast.LENGTH_SHORT);
//                }
//            }
//        });
    }

    private String wxPayCallbackJsMethod = null;

    @JavascriptInterface
    public void setWxPayCallbackJsMethod(String methodName) {
        wxPayCallbackJsMethod = methodName;
    }

    public void setWxPayResult(String result) {
        if (!TextUtils.isEmpty(wxPayCallbackJsMethod)) {
            execJavaScript(wxPayCallbackJsMethod + "('" + result + "');");
        }
    }

    private String backPressedCallBackMethod = null;

    @JavascriptInterface
    public void setBackPressedCallBackMethod(String backPressedCallBackMethod) {
        this.backPressedCallBackMethod = backPressedCallBackMethod;
    }

    public void onBackPressd() {
        if (!TextUtils.isEmpty(backPressedCallBackMethod)) {
            execJavaScript(backPressedCallBackMethod + "();");
        }
    }

    @JavascriptInterface
    public void finish() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mContext instanceof Activity) {
                    ((Activity) mContext).finish();
                }
            }
        });
    }

    @JavascriptInterface
    public void showToastShort(final String msg) {
        Log.d(TAG, "showToastShort:" + msg);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

//    @JavascriptInterface
//    public void navi(final String latStr, final String lonStr, final String describle) {
//        Log.d(TAG, "navi:");
//        mHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    double lat = Double.parseDouble(latStr);
//                    double lon = Double.parseDouble(lonStr);
//                    MapUtil.navi(mContext, lat, lon, describle);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }

//    @JavascriptInterface
//    public void naviByBaidu(final String latStr, final String lonStr, final String describle) {
//        mHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    double lat = Double.parseDouble(latStr);
//                    double lon = Double.parseDouble(lonStr);
//                    MapUtil.naviByBaiduMap(mContext, lat, lon, describle);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }

//    @JavascriptInterface
//    public void naviByGaode(final String latStr, final String lonStr, final String describle) {
//        mHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    double lat = Double.parseDouble(latStr);
//                    double lon = Double.parseDouble(lonStr);
//                    MapUtil.naviByGaoDeMap(mContext, lat, lon, describle);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }

//    @JavascriptInterface
//    public String getInstalledMapInfo() {
//        return MapUtil.getInstalledMapInfo(mContext);
//    }

    @JavascriptInterface
    public void bindPushAlias(final String userId) {
        Log.d(TAG, "bindPushAlias: " + userId);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                JPushInterface.setAlias(mContext, userId, new TagAliasCallback() {
                    @Override
                    public void gotResult(int i, String s, Set<String> set) {
                    }
                });
            }
        });
    }

    @JavascriptInterface
    public void checkUpdate() {
        CheckUpdateHelper.checkUpdate(mContext, null);
    }

    @JavascriptInterface
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivity = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }

    private String inputChangeCallBackMethod = null;

    @JavascriptInterface
    public void setInputChangeCallBackMethod(String inputChangeCallBackMethod) {
        this.inputChangeCallBackMethod = inputChangeCallBackMethod;
    }

    public void onInputChange(boolean isInputWindowShow) {
        String result = isInputWindowShow ? "1" : "0";
        if (!TextUtils.isEmpty(inputChangeCallBackMethod)) {
            execJavaScript(inputChangeCallBackMethod + "('" + result + "');");
        }
    }

    @Override
    public void addJsInterface(String objectName) {
        mWebView.addJavascriptInterface(this, objectName);
    }
}
