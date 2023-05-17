package com.ad.blackboxrecorder.playing;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import com.ad.blackboxrecorder.MainActivity;

import java.io.File;

public class PlayingHandler {
    private final MainActivity mainActivity;

    public PlayingHandler(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    public void playFile(){
        int sampleRateInHz = 44100;  // Adjust this according to your audio format
        int bufferSizeInBytes = AudioTrack.getMinBufferSize(sampleRateInHz, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
        AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRateInHz, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSizeInBytes, AudioTrack.MODE_STREAM);
        audioTrack.play();

        File inputFile = new File(mainActivity.getExternalFilesDir(null), "encrypted_output_0.txt");  // Replace with the actual file name
        DecryptionThread decryptionThread = new DecryptionThread(inputFile, audioTrack);
        decryptionThread.start();
    }
}
