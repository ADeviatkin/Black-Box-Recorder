package com.ad.blackboxrecorder.recording;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

public class EncryptionThread extends Thread {
    private static final long TEN_MINUTES_DATA_SIZE = 52920000;  // Adjust this according to your audio format
    private BlockingQueue<byte[]> queue;
    private File outputDir;
    private long currentFileSize = 0;
    private int fileCounter = 0;
    private FileOutputStream currentOutputStream;

    public EncryptionThread(BlockingQueue<byte[]> queue, File outputDir) {
        this.queue = queue;
        this.outputDir = outputDir;
    }

    public void run() {
        try {
            openNewFile();
            while (true) {
                try {
                    byte[] buffer = queue.take();
                    if (currentFileSize + buffer.length > TEN_MINUTES_DATA_SIZE) {
                        closeCurrentFile();
                        openNewFile();
                    }
                    for (int i = 0; i < buffer.length; i++) {
                        buffer[i] = (byte) (buffer[i] + 1);  // Simple Caesar cipher for encryption
                    }
                    currentOutputStream.write(buffer);
                    currentFileSize += buffer.length;
                } catch (InterruptedException e) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeCurrentFile();
        }
    }

    private void openNewFile() throws FileNotFoundException {
        File outputFile = new File(outputDir, "encrypted_output_" + fileCounter + ".txt");
        currentOutputStream = new FileOutputStream(outputFile);
        fileCounter++;
        currentFileSize = 0;
    }

    private void closeCurrentFile() {
        if (currentOutputStream != null) {
            try {
                currentOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}