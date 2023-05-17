package com.ad.blackboxrecorder;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import com.ad.blackboxrecorder.R;

import java.io.File;

public class Explorer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_listen);

        ListView listView = (ListView) findViewById(R.id.listView);

        // Specify the directory to list files from
        File directory = new File(String.valueOf(this.getExternalFilesDir(null)));
        File[] files = directory.listFiles();
        if (files != null) {
            //FileAdapter adapter = new FileAdapter(this, files);
            //listView.setAdapter(adapter);
        } else {
            // Handle the case where files is null
        }

    }
}
