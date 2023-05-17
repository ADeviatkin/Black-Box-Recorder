package com.ad.blackboxrecorder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.ad.blackboxrecorder.gui.ButtonManager2;
import com.ad.blackboxrecorder.gui.MainAdapter;
import com.ad.blackboxrecorder.gui.tabs.ListenPage;
import com.ad.blackboxrecorder.gui.tabs.RecordPage;
import com.ad.blackboxrecorder.service.ServiceBridge;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    private ServiceBridge ServiceBridge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ServiceBridge = new ServiceBridge(this);
        //new ButtonManager(findViewById(android.R.id.content), new ButtonHandler(), ServiceBridge);

        //RecordingHandler RecordingHandler = new RecordingHandler(this);
        //RecordingHandler.start();
        //PlayingHandler PlayingHandler = new PlayingHandler(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ViewPager viewPager = findViewById(R.id.view_pager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);

        MainAdapter mainAdapter = new MainAdapter(getSupportFragmentManager());

        // Add your fragments to the adapter
        mainAdapter.addFragment(new RecordPage(), "RECORD");
        mainAdapter.addFragment(new ListenPage(), "LISTEN");

        // Set the adapter on the ViewPager
        viewPager.setAdapter(mainAdapter);

        // Connect the TabLayout and the ViewPager
        tabLayout.setupWithViewPager(viewPager);
    }

}