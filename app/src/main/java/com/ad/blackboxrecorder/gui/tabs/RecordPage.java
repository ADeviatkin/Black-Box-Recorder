package com.ad.blackboxrecorder.gui.tabs;

import android.annotation.SuppressLint;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ad.blackboxrecorder.R;
import com.ad.blackboxrecorder.gui.ButtonManager;
import com.gauravk.audiovisualizer.visualizer.CircleLineVisualizer;

public class RecordPage extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_record, container, false);
    }

    private static final int SAMPLE_RATE = 44100;
    private static final int CHANNEL = AudioFormat.CHANNEL_IN_MONO;
    private static final int FORMAT = AudioFormat.ENCODING_PCM_16BIT;

    AudioRecord audioRecord;
    int bufferSize;

    @SuppressLint("MissingPermission")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        CircleLineVisualizer mVisualizer = view.findViewById(R.id.blast);
        bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL, FORMAT);
        new ButtonManager(view);
        ButtonManager.handleRecordingButton();

        //audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE, CHANNEL, FORMAT, bufferSize);
        //audioRecord.startRecording();
        /*
        Thread recordingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                byte audioData[] = new byte[bufferSize];
                while (audioRecord.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING) {
                    audioRecord.read(audioData, 0, 1000);
                    mVisualizer.setRawAudioBytes(audioData);
                }
            }
        }, "AudioRecorder Thread");

        recordingThread.start();
         */

    }
}
