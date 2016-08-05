package com.juma.truckdoctor.js.helper;

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.juma.truckdoctor.js.R;
import com.juma.truckdoctor.js.dialog.AlertDialogFragment;
import com.juma.truckdoctor.js.manager.PermissionManager;
import com.juma.truckdoctor.js.model.CheckUpdateParam;
import com.juma.truckdoctor.js.model.CheckUpdateResponse;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/6/12 0012.
 */
public class CheckUpdateHelper {

    private static long downloadId = 0;

    private static String downloadUrl = null;

    public static void checkUpdate(final Context context, final PermissionManager permissionManager) {
        CheckUpdateParam param = new CheckUpdateParam();
        param.packageName = context.getPackageName();
        try {
            param.versionCode = context.getPackageManager().getPackageInfo(param.packageName, 0).versionCode;
        } catch (Exception e) {
            param.versionCode = 0;
        }
        param.platform = "A";

        String url = "";
        if(TextUtils.isEmpty(url))
            return;

        OkHttpUtils.post()
                .url(url)
                .addParams("", "")
                .addParams("", "")
                .addParams("", "")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int code = jsonObject.getInt("code");
                    String msg = jsonObject.getString("message");
                    if (code == 0 && jsonObject.has("data")) {
                        final CheckUpdateResponse resp = new Gson().fromJson(jsonObject.getString("data"),
                                CheckUpdateResponse.class);
                        if (TextUtils.isEmpty(resp.downloadUrl)) {
                            return;
                        }
                        downloadUrl = resp.downloadUrl;
                        new AlertDialogFragment.Builder(context)
                                .setTitle(R.string.update_tips)
                                .setMessage(resp.remark)
                                .setNegativeButton(R.string.update_cancel, null)
                                .setPositiveButton(R.string.update_ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (permissionManager != null) {
                                            permissionManager.checkAndRequestPermissions(PermissionManager.REQUEST_CODES_PERMISSON_STORAGE);
                                        } else {
                                            startDownload(context);
                                        }
                                    }
                                })
                                .createDialog()
                                .show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    public static void startDownload(Context context) {
        if (TextUtils.isEmpty(downloadUrl)) {
            return;
        }
        try {
            downloadId = startDownload(context, downloadUrl);
            downloadUrl = null;
        } catch (Exception e) {
        }
    }

    public static long getDownloadId() {
        return downloadId;
    }

    private static long startDownload(Context context, String url) {
        if (context == null) {
            return -1;
        }

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        //设置允许使用的网络类型，这里是移动网络和wifi都可以
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE
                | DownloadManager.Request.NETWORK_WIFI);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setMimeType("application/vnd.android.package-archive");
        request.allowScanningByMediaScanner();
        request.setVisibleInDownloadsUi(true);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "hello_driver_update.apk");

        return downloadManager.enqueue(request);
    }
}
