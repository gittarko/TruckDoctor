package com.juma.truckdoctor.js.webview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Administrator on 2016/6/7 0007.
 */
public abstract class HDWebViewClient extends WebViewClient {

    private Context context;

    public HDWebViewClient(Context context) {
        this.context = context;
    }

    public abstract void showErrorView();

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (!TextUtils.isEmpty(url)) {
            Uri uri = Uri.parse(url);
            String schema = uri.getScheme();
            if (TextUtils.equals(schema, "mailto") || TextUtils.equals(schema, "geo") || TextUtils.equals(schema, "tel")) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                context.startActivity(intent);
                return true;
            }
        }
        return super.shouldOverrideUrlLoading(view, url);
    }
//
//    @Override
//    public void onLoadResource(WebView view, String url) {
//        super.onLoadResource(view, url);
//        if (url != null && url.equals("file:///android_asset/webkit/android-weberror.png")) {
//            showErrorView();
//        }
//    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
        showErrorView();
    }
//
//    @Override
//    public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
//        super.onReceivedHttpError(view, request, errorResponse);
//        showErrorView();
//    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
        showErrorView();
    }
}
