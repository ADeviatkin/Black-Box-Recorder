package com.ad.blackboxrecorder.activities.Main;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.ad.blackboxrecorder.R;
import com.ad.blackboxrecorder.activities.Main.gui.MenuHandler;
import com.ad.blackboxrecorder.service.ServiceBridge;
import com.ad.blackboxrecorder.utils.PermissionUtils;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    public static File recordingsDirectory;
    public static ServiceBridge ServiceBridge;

    private static final String[] REQUIRED_PERMISSIONS = new String[]{
            android.Manifest.permission.RECORD_AUDIO,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.FOREGROUND_SERVICE,
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //PermissionManager.grantPermissions(this);
        PermissionUtils.requestPermissions(this, REQUIRED_PERMISSIONS);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PermissionUtils.PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    performOperationThatNeedsPermissions();
                } else {
                    //handlePermissionDenied();
                }
                break;
        }
    }

    public void performOperationThatNeedsPermissions() {
        new MenuHandler(this);
        ServiceBridge = new ServiceBridge(this);
        recordingsDirectory = this.getExternalFilesDir(null);
    }



}
