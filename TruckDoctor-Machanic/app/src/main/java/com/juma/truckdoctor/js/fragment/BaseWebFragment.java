package com.juma.truckdoctor.js.fragment;


import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.juma.truckdoctor.js.R;
import com.juma.truckdoctor.js.activity.MainWebActivity;
import com.juma.truckdoctor.js.api.ApiResponse;
import com.juma.truckdoctor.js.api.ApiWeb;
import com.juma.truckdoctor.js.common.Constants;
import com.juma.truckdoctor.js.helper.CheckUpdateHelper;
import com.juma.truckdoctor.js.manager.PermissionManager;
import com.juma.truckdoctor.js.webview.BaseJsInterface;
import com.juma.truckdoctor.js.webview.HDWebChromeClient;
import com.juma.truckdoctor.js.webview.HDWebViewClient;
import com.juma.truckdoctor.js.webview.JsInterface;
import com.juma.truckdoctor.js.webview.WebviewHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseWebFragment extends BackHandledFragment implements View.OnClickListener {

    private static final String TAG = BaseWebFragment.class.getSimpleName();
    public static final String ARG_PARAM1 = "param1";
    public String JS_INTERFACE_NAME = "platform";

    protected String uriString;

    protected View webContent;
    protected ViewGroup parentView;
    protected View mButtonBack;
    protected TextView mHeaderTitle;
    protected View mHeaderView;

    protected View mPageLoading;
    protected View mErrorRefresh;
    protected View mRefreshButton;

    //网页浏览器
    protected WebView webView;
    protected BaseJsInterface mWebInterface;

    protected WebViewClient mWebViewClient;
    protected HDWebChromeClient mWebChromeClient;

    private PermissionManager permissionManager;

//    public static BaseWebFragment newInstance(String url) {
//        BaseWebFragment fragment = new BaseWebFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, url);
//        fragment.setArguments(args);
//        return fragment;
//    }

    public BaseWebFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            uriString = getArguments().getString(ARG_PARAM1);
        }

        //Just for test
//        startActivity(new Intent(getActivity(), TestActivity.class));
    }

    public void loadUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        uriString = url;

        if (webView != null) {
            webView.loadUrl(uriString);
        }
    }

    /**
     * 加载未处理订单数字
     */
    protected void requestNoticeData() {

    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        permissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }

    public void onActivityRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        permissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(webContent == null) {
            runNewInstance(inflater, container);
        }else {
            runOlderInstance(webContent);
        }

        return webContent;
    }

    /**
     * 存在缓存视图使用
     * @param webContent
     */
    private void runOlderInstance(View webContent) {
        ViewGroup parent = (ViewGroup)webContent.getParent();
        if(parent != null) {
            parent.removeView(webContent);
        }
    }

    /**
     * Fragment视图首次被创建时使用
     * @param inflater
     * @param container
     * @return
     */
    protected View runNewInstance(LayoutInflater inflater, ViewGroup container) {
        webContent = inflater.inflate(R.layout.web_content_layout, container, false);
        parentView = (ViewGroup) webContent.findViewById(R.id.parent_view);
        mHeaderView = webContent.findViewById(R.id.header_area);
        mButtonBack = mHeaderView.findViewById(R.id.buttonBack);
        mButtonBack.setOnClickListener(this);
        mHeaderTitle = (TextView) mHeaderView.findViewById(R.id.textTitle);

        webView = (WebView) webContent.findViewById(R.id.webView_content);
        mErrorRefresh = webContent.findViewById(R.id.refresh_page);
        mRefreshButton = mErrorRefresh.findViewById(R.id.refresh_btn);

        mRefreshButton.setVisibility(View.VISIBLE);
        mRefreshButton.setOnClickListener(this);
        mPageLoading = webContent.findViewById(R.id.loadingProgressBar);

        if (TextUtils.isEmpty(uriString)) {
            getActivity().finish();
            return webContent;
        }

        //注册广播
        registReceiver();
        //初始化webView
        initWebView(JS_INTERFACE_NAME);
        //初始化WebView监听
        listenerSoftInput();

        return webContent;
    }

    private void listenerSoftInput() {
        webView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(webView == null) {
                    return;
                }
                if(mWebInterface == null) {
                    return;
                }

                Log.d(TAG, "getDecorView().getHeight(): " + getActivity().getWindow().getDecorView().getHeight());
                Log.d(TAG, "webView.getHeight(): " + webView.getHeight());
                int heightDiff = Math.abs(getActivity().getWindow().getDecorView().getHeight() - webView.getHeight());
                if (heightDiff > 100) {
                    mWebInterface.onInputChange(true);
                } else {
                    mWebInterface.onInputChange(false);
                }
            }
        });
    }

    @SuppressLint("NewApi")
    protected void initWebView(String jsInterfaceName) {
        mWebViewClient = new HDWebViewClient(getActivity()) {
            @Override
            public void showErrorView() {
                if (mErrorRefresh != null) {
                    mErrorRefresh.post(new Runnable() {
                        @Override
                        public void run() {
                            mErrorRefresh.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        };

        mWebChromeClient = new HDWebChromeClient(getActivity());

        webView.setWebViewClient(mWebViewClient);
        webView.setWebChromeClient(mWebChromeClient);

        WebviewHelper.configWebView(webView);

        if (Build.VERSION.SDK_INT >= 19) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        //设置web调用java对象
        mWebInterface = addJavaScriptInterface(JS_INTERFACE_NAME);

        webView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                if(webView == null)
                    return;
                webView.postInvalidate();
            }
        });

        webView.setDownloadListener(onDownloadListener);
        //同步cookies
        WebviewHelper.synCookies(getActivity(), uriString);

        loadUrl(uriString);

        //测试
//        String htmlText = AssetsHelper.getStringAssets(this, "test.html");
//        webView.loadData(htmlText, "text/html", "utf-8");
    }

    public abstract BaseJsInterface addJavaScriptInterface(String objectName);

    public HDWebChromeClient getWebChromeClient() {
        return mWebChromeClient;
    }

    private DownloadListener onDownloadListener = new DownloadListener() {
        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                    long contentLength) {
            try {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onResume() {
        if (webView != null) {
//            webView.invalidate();
            webView.getSettings().setJavaScriptEnabled(true);
            if (Build.VERSION.SDK_INT >= 11) {
                webView.onResume();
            }
        }

        CookieSyncManager.getInstance().startSync();
        super.onResume();
    }

    @Override
    public void onPause() {
        CookieSyncManager.getInstance().stopSync();
        if (webView != null) {
            webView.getSettings().setJavaScriptEnabled(false);
            if (Build.VERSION.SDK_INT >= 11) {
                webView.onPause();
            }
        }
        super.onPause();
    }

    @Override
    public void onDestroyView() {
//        if (webView != null) {
//            webView.stopLoading();
//            webView.setWebChromeClient(null);
//            webView.setWebViewClient(null);
//            webView.getSettings().setJavaScriptEnabled(false);
//            webView.loadDataWithBaseURL("about:blank", "<html></html>", "text/html", "UTF-8", null);
//            webView.removeAllViews();
//            webView.destroy();
//            webView = null;
//        }
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(wxCallbackReceiver);
    }

    protected void onClickRefresh() {
        mErrorRefresh.setVisibility(View.GONE);
        webView.reload();
    }

    @Override
    public boolean onBackPressed() {
        if (mErrorRefresh.getVisibility() == View.VISIBLE) {
            return false;
        } else {
            if(mWebInterface != null) {
                mWebInterface.onBackPressd();
            }
            return true;
        }
    }

//    private boolean goBack() {
//        if (webView.canGoBack()) {
//            webView.goBack();
//            return true;
//        }
//        return false;
//    }

    @Override
    public void onClick(View v) {
        if (v.getId() == mButtonBack.getId()) {
        } else if (v.getId() == mRefreshButton.getId()) {
            onClickRefresh();
        }
    }

    private static final String WX_CALLBACK_ACTION = "com.receiver.action.wx.callback";
    private static final String EXTRA_WX_RET = "extra_wx_ret";

    protected void registReceiver() {
        IntentFilter filter = new IntentFilter();
        addReceiverAction(filter);
        BroadcastReceiver receiver = getBroadcastReceiver();
        if(receiver != null) {
            getActivity().registerReceiver(receiver, filter);
        }
    }

    protected void addReceiverAction(IntentFilter filter) {
        filter.addAction(WX_CALLBACK_ACTION);
        filter.addAction(Constants.INTENT_ACTION_NOTICE);
        filter.addAction(Constants.INTENT_ACTION_NOTICE_REQUEST);
    }

    BroadcastReceiver wxCallbackReceiver = null;
    protected BroadcastReceiver getBroadcastReceiver() {
        //用于接收微信回调的广播
        wxCallbackReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (TextUtils.equals(WX_CALLBACK_ACTION, action)) {
                    String ret = intent.getStringExtra(EXTRA_WX_RET);
//                mWebInterface.setWxPayResult(ret);
//                if (TextUtils.equals(ret, "0")) {
//                    mWebInterface.updatePayWaybillStatusComplete();
//                } else {
//                    mWebInterface.updatePayWaybillStatusOriginal();
//                }
                }else if(intent.getAction().equals(Constants.INTENT_ACTION_NOTICE)) {

                }else if(intent.getAction().equals(Constants.INTENT_ACTION_NOTICE_REQUEST)) {
                    requestNoticeData();
                }
            }
        };

        return wxCallbackReceiver;
    }

    public static void sendWxCallbackBroadcast(Context context, String ret) {
        Intent intent = new Intent(WX_CALLBACK_ACTION);
        intent.putExtra(EXTRA_WX_RET, ret);
        context.sendBroadcast(intent);
    }
}
