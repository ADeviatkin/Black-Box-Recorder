package com.ad.blackboxrecorder.gui.tabs;

import android.annotation.SuppressLint;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ad.blackboxrecorder.MainActivity;
import com.ad.blackboxrecorder.R;
import com.ad.blackboxrecorder.gui.ButtonManager;
import com.tyorikan.voicerecordingvisualizer.RecordingSampler;
import com.tyorikan.voicerecordingvisualizer.VisualizerView;

import java.io.IOException;

public class RecordPage extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_record, container, false);
    }
    @SuppressLint("MissingPermission")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        new ButtonManager(view);
        ButtonManager.handleRecordingButton();

        VisualizerView visualizerView = (VisualizerView) getActivity().findViewById(R.id.visualizer);

        RecordingSampler recordingSampler = new RecordingSampler();
        //recordingSampler.setVolumeListener(getActivity());  // for custom implements
        recordingSampler.setSamplingInterval(100); // voice sampling interval
        recordingSampler.link(visualizerView);     // link to visualizer

        recordingSampler.startRecording();
    }
}
