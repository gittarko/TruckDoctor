package com.juma.truckdoctor.js.receiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.juma.truckdoctor.js.helper.CheckUpdateHelper;

import java.io.File;
import java.net.URI;

public class UpdateInstallReceiver extends BroadcastReceiver {

    private static final String TAG = UpdateInstallReceiver.class.getSimpleName();

    public UpdateInstallReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
        if (downloadId == CheckUpdateHelper.getDownloadId()) {
            try {
                DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                Uri orignalUri = downloadManager.getUriForDownloadedFile(downloadId);
                Uri targetUri = Uri.fromFile(new File(new URI(orignalUri.toString())));
                Log.d(TAG, "targetUri: " + targetUri.toString());
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setDataAndType(targetUri, "application/vnd.android.package-archive");
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
