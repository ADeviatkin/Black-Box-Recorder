package com.ad.blackboxrecorder.recording;

import android.annotation.SuppressLint;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import java.util.concurrent.BlockingQueue;

public class RecordingThread extends Thread {
    private AudioRecord audioRecord;
    private int bufferSize;
    private byte[] buffer;
    private BlockingQueue<byte[]> queue;

    @SuppressLint("MissingPermission")
    public RecordingThread(BlockingQueue<byte[]> queue) {
        this.queue = queue;
        bufferSize = AudioRecord.getMinBufferSize(44100,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT);
        buffer = new byte[bufferSize];
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                44100,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize);
    }

    public void run() {
        audioRecord.startRecording();
        while (true) {
            audioRecord.read(buffer, 0, bufferSize);
            try {
                queue.put(buffer);
            } catch (InterruptedException e) {
                break;
            }
        }
        audioRecord.stop();
        audioRecord.release();
    }
}
