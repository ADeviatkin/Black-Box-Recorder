package com.ad.blackboxrecorder.activities.Main.model.playing;

import android.media.AudioTrack;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Decryptor {
    public static byte[] decrypt(File inputFile) {
        try (FileInputStream fis = new FileInputStream(inputFile);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];
            int read;
            while ((read = fis.read(buffer)) != -1) {
                for (int i = 0; i < read; i++) {
                    buffer[i] = (byte) (buffer[i] - 1);  // Reverse the Caesar cipher
                }
                bos.write(buffer, 0, read);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }
}
