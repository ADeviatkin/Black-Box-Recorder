package com.ad.blackboxrecorder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;

import com.ad.blackboxrecorder.gui.MainAdapter;
import com.ad.blackboxrecorder.gui.tabs.ListenPage;
import com.ad.blackboxrecorder.gui.tabs.RecordPage;
import com.ad.blackboxrecorder.recording.RecordingHandler;
import com.ad.blackboxrecorder.service.RecordingService;
import com.ad.blackboxrecorder.service.ServiceBridge;
import com.google.android.material.tabs.TabLayout;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    public static File directory;
    TabLayout tabLayout;
    ViewPager viewPager;
    public static ServiceBridge ServiceBridge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ServiceBridge = new ServiceBridge(this);
        //PlayingHandler PlayingHandler = new PlayingHandler(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        directory = this.getExternalFilesDir(null);

        ViewPager viewPager = findViewById(R.id.view_pager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);

        MainAdapter mainAdapter = new MainAdapter(getSupportFragmentManager());

        // Add your fragments to the adapter
        mainAdapter.addFragment(new RecordPage(), "RECORD");
        mainAdapter.addFragment(new ListenPage(), "LISTEN");

        // Set the adapter on the ViewPager
        viewPager.setAdapter(mainAdapter);
        tabLayout.setupWithViewPager(viewPager);
        ServiceBridge.isServiceRunning(RecordingService.class);
    }
}
