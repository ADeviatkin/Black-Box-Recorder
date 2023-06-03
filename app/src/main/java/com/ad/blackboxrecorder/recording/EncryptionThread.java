package com.ad.blackboxrecorder.recording;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

public class EncryptionThread extends Thread {

    private BlockingQueue<byte[]> queue;

    public EncryptionThread(BlockingQueue<byte[]> queue) {
        this.queue = queue;
    }

    public void run() {
        Record newOneRecord = null;
        try {
            newOneRecord = new Record();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            while (RecordingHandler.isThreadOn) {
                try {
                    byte[] buffer = queue.take();
                    if (newOneRecord.getFileSize() + buffer.length > newOneRecord.getDataSize()) {
                        newOneRecord = new Record();
                    }
                    for (int i = 0; i < buffer.length; i++) {
                        buffer[i] = (byte) (buffer[i] + 1);  // Simple Caesar cipher for encryption
                    }
                    newOneRecord.writeToFile(buffer);
                } catch (InterruptedException e) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            newOneRecord = null;
        }
    }
}