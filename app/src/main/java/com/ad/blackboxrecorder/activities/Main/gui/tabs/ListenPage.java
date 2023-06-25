package com.ad.blackboxrecorder.activities.Main.gui.tabs;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ad.blackboxrecorder.R;
import com.ad.blackboxrecorder.activities.Main.adapter.FileAdapter;

import java.io.File;
import java.util.ArrayList;

public class ListenPage extends Fragment {
    private TextView listenMessage;
    private ListView recordingsListView;
    private ArrayAdapter<String> recordingElementAdapter;
    private ArrayList<String> files;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_listen, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recordingsListView = view.findViewById(R.id.listView);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        listenMessage = view.findViewById(R.id.listen_message);

        files = new ArrayList<>();
        recordingElementAdapter = new FileAdapter(requireActivity(), files);
        recordingsListView.setAdapter(recordingElementAdapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateListView();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        updateListView();
    }

    public void updateListView() {
        File ExternalDirectory = requireActivity().getExternalFilesDir(null);
        if (ExternalDirectory != null && ExternalDirectory.isDirectory()) {
            files.clear();
            File[] fileList = ExternalDirectory.listFiles();
            if (fileList != null) {
                for (File file : fileList) {
                    if (file.getName().split("\\.")[1].equals("encraud"))
                        files.add(file.getName());
                }
                recordingElementAdapter.notifyDataSetChanged();
            }
            if (files.isEmpty()) {
                listenMessage.setVisibility(View.VISIBLE);
            } else {
                listenMessage.setVisibility(View.INVISIBLE);
            }
        }
    }

}