package com.ad.blackboxrecorder.gui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;

import com.ad.blackboxrecorder.R;

import java.util.ArrayList;

public class FileAdapter extends ArrayAdapter<String> {

    private Context context;
    private ArrayList<String> files;

    public FileAdapter(Context context, ArrayList<String> files) {
        super(context, R.layout.list_item, files);
        this.context = context;
        this.files = files;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.textView);
        ImageButton playButton = convertView.findViewById(R.id.playButton);

        String fileName = files.get(position);
        textView.setText(fileName.substring(0,fileName.lastIndexOf(".")));

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle play button click
            }
        });

        return convertView;
    }
}