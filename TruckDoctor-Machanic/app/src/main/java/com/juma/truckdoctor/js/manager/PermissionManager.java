package com.juma.truckdoctor.js.manager;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;

import com.juma.truckdoctor.js.R;
import com.juma.truckdoctor.js.dialog.AlertDialogFragment;
import com.juma.truckdoctor.js.helper.CheckUpdateHelper;

/**
 * Created by Administrator on 2016/6/14 0014.
 */
public class PermissionManager extends BasePermissionManager {

    public static final int REQUEST_CODES_ALL_PERMISSON = 0;
    public static final int REQUEST_CODES_PERMISSON_STORAGE = 1;

    public PermissionManager(Activity activity) {
        super(activity);
    }

    @Override
    protected String[][] getPermissions() {
        return new String[][]{
                new String[]{
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                },
                new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }
        };
    }

    @Override
    protected int[] getPermissionsRequestCodes() {
        return new int[]{
                REQUEST_CODES_ALL_PERMISSON,
                REQUEST_CODES_PERMISSON_STORAGE
        };
    }

    @Override
    protected boolean allowShowRequestPermissionRationale() {
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODES_PERMISSON_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                CheckUpdateHelper.startDownload(activity);
            } else {
                showPermissionDialog(activity);
            }
        }
    }

    @Override
    public void onShowRequestPermissionRationale(Activity activity, String permission, int requestCode) {
        if (requestCode == REQUEST_CODES_PERMISSON_STORAGE) {
            showPermissionDialog(activity);
        }
    }

    @Override
    public void onCheckSelfPermissionGranted(Activity activity, String permission, int requestCode) {
        if (requestCode == REQUEST_CODES_PERMISSON_STORAGE) {
            CheckUpdateHelper.startDownload(activity);
        }
    }

    private void showPermissionDialog(final Activity activity) {
        new AlertDialogFragment.Builder(activity)
                .setTitle(R.string.permissin_title)
                .setMessage(R.string.permission_msg)
                .setPositiveButton(R.string.permissin_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.fromParts("package", activity.getPackageName(), null));
                        activity.startActivity(intent);
                    }
                })
                .createDialog()
                .show();
    }
}
