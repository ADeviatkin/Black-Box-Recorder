package com.ad.blackboxrecorder.gui.tabs;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ad.blackboxrecorder.R;
import com.ad.blackboxrecorder.gui.FileAdapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.File;
import java.util.ArrayList;

public class ListenPage extends Fragment {

    private ArrayAdapter<String> adapter;
    private ArrayList<String> files;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_listen, container, false);
    }
    public BottomSheetBehavior bottomSheetBehavior;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView listView = view.findViewById(R.id.listView);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);

        files = new ArrayList<>();
        //adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_list_item_1, files);
        adapter = new FileAdapter(requireActivity(), files);
        listView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateListView();
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        View bottomSheet = view.findViewById(R.id.bottom_sheet);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomSheet.getLayoutParams();
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);


        // To expand the bottom sheet
        //bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        // To collapse the bottom sheet
        //bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        // To hide the bottom sheet
        //bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        updateListView();
    }

    private void updateListView() {
        File directory = requireActivity().getExternalFilesDir(null);
        if (bottomSheetBehavior.getState()!=BottomSheetBehavior.STATE_EXPANDED){
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
        else{
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }

        // Check if the directory is not null and is a directory
        if (directory != null && directory.isDirectory()) {
            // Get the list of files in the directory
            File[] fileList = directory.listFiles();

            // Check if the file list is not null
            if (fileList != null) {
                // Clear the ArrayList and add the names of the files
                files.clear();
                for (File file : fileList) {
                    files.add(file.getName());
                }
                // Notify the ArrayAdapter that the data has changed
                adapter.notifyDataSetChanged();
            }
        }
    }
}