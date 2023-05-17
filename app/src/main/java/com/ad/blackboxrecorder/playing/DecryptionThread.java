package com.ad.blackboxrecorder.playing;

import android.media.AudioTrack;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class DecryptionThread extends Thread {
    private File inputFile;
    private AudioTrack audioTrack;

    public DecryptionThread(File inputFile, AudioTrack audioTrack) {
        this.inputFile = inputFile;
        this.audioTrack = audioTrack;
    }

    public void run() {
        try {
            FileInputStream fis = new FileInputStream(inputFile);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = fis.read(buffer)) != -1) {
                for (int i = 0; i < read; i++) {
                    buffer[i] = (byte) (buffer[i] - 1);  // Reverse the Caesar cipher
                }
                audioTrack.write(buffer, 0, read);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
