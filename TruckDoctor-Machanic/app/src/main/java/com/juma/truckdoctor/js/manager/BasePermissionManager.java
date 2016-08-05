package com.juma.truckdoctor.js.manager;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hedong on 16/8/5.
 */

public abstract class BasePermissionManager {
    protected Activity activity;
    private Map<Integer, String[]> permissionMap;

    public BasePermissionManager(Activity activity) {
        this.activity = activity;
        String[][] permissionsArray = getPermissions();
        int[] permissionsRequestCodes = getPermissionsRequestCodes();
        try {
            if (permissionsArray.length == permissionsRequestCodes.length) {
                permissionMap = new HashMap<>();
                for (int i = 0; i < permissionsArray.length; i++) {
                    if (permissionsArray[i].length > 0) {
                        permissionMap.put(permissionsRequestCodes[i], permissionsArray[i]);
                    }
                }
            } else {
                throw new IllegalArgumentException("permissions and request code must have same length");
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public void checkAndRequestPermissions(int requestCode) {
        if (permissionMap.containsKey(requestCode)) {
            String[] permissions = permissionMap.get(requestCode);
            if (permissions.length == 1) {
                checkAndRequestPermission(permissions[0], requestCode);
            } else {
                checkAndRequestPermissions(permissions, requestCode);
            }
        }
    }

    private void checkAndRequestPermissions(String[] permissions, int requestCode) {
        if (permissions != null && permissions.length > 0) {
            List<String> permissionsNeedGrant = new ArrayList<>();
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                    permissionsNeedGrant.add(permission);
                }
            }
            if (permissionsNeedGrant.size() > 0) {
                String[] permissionsToGrant = new String[permissionsNeedGrant.size()];
                for (int i = 0; i < permissionsNeedGrant.size(); i++) {
                    permissionsToGrant[i] = permissionsNeedGrant.get(i);
                }
                ActivityCompat.requestPermissions(activity, permissionsToGrant, requestCode);
            }
        }
    }

    private void checkAndRequestPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
            if (allowShowRequestPermissionRationale()) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                    onShowRequestPermissionRationale(activity, permission, requestCode);
                } else {
                    ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
                }
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
            }
        } else {
            onCheckSelfPermissionGranted(activity, permission, requestCode);
        }
    }

    protected boolean allowShowRequestPermissionRationale() {
        return true;
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    }

    public void onShowRequestPermissionRationale(Activity activity, String permission, int requestCode) {
    }

    public void onCheckSelfPermissionGranted(Activity activity, String permission, int requestCode) {
    }

    protected abstract String[][] getPermissions();

    protected abstract int[] getPermissionsRequestCodes();

}
