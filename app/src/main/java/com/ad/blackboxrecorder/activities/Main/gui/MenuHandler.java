package com.ad.blackboxrecorder.activities.Main.gui;

import androidx.viewpager.widget.ViewPager;

import com.ad.blackboxrecorder.activities.Main.MainActivity;
import com.ad.blackboxrecorder.R;
import com.ad.blackboxrecorder.activities.Main.adapter.MainAdapter;
import com.ad.blackboxrecorder.activities.Main.gui.tabs.ListenPage;
import com.ad.blackboxrecorder.activities.Main.gui.tabs.RecordPage;
import com.google.android.material.tabs.TabLayout;

public class MenuHandler {
    private final MainActivity mainActivity;

    public MenuHandler(MainActivity mainActivity){
        this.mainActivity = mainActivity;
        initMenu();
    }
    private void initMenu(){
        ViewPager viewPager = this.mainActivity.findViewById(R.id.view_pager);
        TabLayout tabLayout = this.mainActivity.findViewById(R.id.tab_layout);
        MainAdapter mainAdapter = new MainAdapter(this.mainActivity.getSupportFragmentManager());
        mainAdapter.addFragment(new RecordPage(), "RECORD");
        mainAdapter.addFragment(new ListenPage(), "LISTEN");
        viewPager.setAdapter(mainAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
