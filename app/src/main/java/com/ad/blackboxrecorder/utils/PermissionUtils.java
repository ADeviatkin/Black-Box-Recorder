package com.ad.blackboxrecorder.utils;

import android.app.Activity;

import androidx.core.app.ActivityCompat;

public final class PermissionUtils {
    public static final int PERMISSION_REQUEST_CODE = 1;
    public static void requestPermissions(Activity activity, String[] REQUIRED_PERMISSIONS) {
        ActivityCompat.requestPermissions(activity, REQUIRED_PERMISSIONS, PERMISSION_REQUEST_CODE);
    }
}
