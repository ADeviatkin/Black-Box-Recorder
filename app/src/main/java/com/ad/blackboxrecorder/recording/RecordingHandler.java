package com.ad.blackboxrecorder.recording;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import com.ad.blackboxrecorder.playing.DecryptionThread;
import com.ad.blackboxrecorder.service.RecordingService;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class RecordingHandler {

    private final RecordingService mainActivity;

    public RecordingHandler(RecordingService mainActivity){
        this.mainActivity = mainActivity;
    }

    public void start(){
        BlockingQueue<byte[]> queue = new LinkedBlockingQueue<>();
        File outputDir = mainActivity.getExternalFilesDir(null);
        RecordingThread recordingThread = new RecordingThread(queue);
        EncryptionThread encryptionThread = new EncryptionThread(queue, outputDir);
        recordingThread.start();
        encryptionThread.start();

    }
    public void stop(){
        int sampleRateInHz = 44100;  // Adjust this according to your audio format
        int bufferSizeInBytes = AudioTrack.getMinBufferSize(sampleRateInHz, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
        AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRateInHz, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSizeInBytes, AudioTrack.MODE_STREAM);
        audioTrack.play();

        File inputFile = new File(mainActivity.getExternalFilesDir(null), "test.txt");  // Replace with the actual file name
        DecryptionThread decryptionThread = new DecryptionThread(inputFile, audioTrack);
        decryptionThread.start();
    }
}
