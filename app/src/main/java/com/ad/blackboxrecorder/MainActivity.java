package com.ad.blackboxrecorder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.pm.PackageManager;
import android.os.Bundle;

import com.ad.blackboxrecorder.gui.MainAdapter;
import com.ad.blackboxrecorder.gui.tabs.ListenPage;
import com.ad.blackboxrecorder.gui.tabs.RecordPage;
import com.ad.blackboxrecorder.service.RecordingService;
import com.ad.blackboxrecorder.service.ServiceBridge;
import com.google.android.material.tabs.TabLayout;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    public static File directory;
    public static ServiceBridge ServiceBridge;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!PermissionManager.hasAllPermissions(this)) {
            PermissionManager.requestPermissions(this);
        }
        ServiceBridge = new ServiceBridge(this);
        directory = this.getExternalFilesDir(null);
        ViewPager viewPager = findViewById(R.id.view_pager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        MainAdapter mainAdapter = new MainAdapter(getSupportFragmentManager());
        mainAdapter.addFragment(new RecordPage(), "RECORD");
        mainAdapter.addFragment(new ListenPage(), "LISTEN");
        viewPager.setAdapter(mainAdapter);
        tabLayout.setupWithViewPager(viewPager);
        ServiceBridge.isServiceRunning(RecordingService.class);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PermissionManager.PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        break;
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
