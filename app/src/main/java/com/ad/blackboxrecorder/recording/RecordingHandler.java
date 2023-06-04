package com.ad.blackboxrecorder.recording;

import com.ad.blackboxrecorder.MainActivity;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class RecordingHandler {
    public static boolean isThreadOn;
    public RecordingHandler(){
        Thread thread = new Thread() {
            @Override
            public void run() {
                int time_to_next_removal = updateListViewFromService();
                try {
                    // The thread will sleep for 5000 milliseconds, or 5 seconds
                    Thread.sleep(time_to_next_removal);
                } catch (InterruptedException e) {
                    // This is thrown if another thread interrupts this thread while it's sleeping
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }
    RecordingThread recordingThread;
    EncryptionThread encryptionThread;
    public void start(){
        BlockingQueue<byte[]> queue = new LinkedBlockingQueue<>();
        recordingThread = new RecordingThread(queue);
        encryptionThread = new EncryptionThread(queue);
        isThreadOn = true;
        recordingThread.start();
        encryptionThread.start();
    }
    public void stop(){
        isThreadOn = false;
    }
    public static int updateListViewFromService() {
        File ExternalDirectory = MainActivity.directory;
        int min_time_to_remove = 60*60;
        if (ExternalDirectory != null && ExternalDirectory.isDirectory()) {
            File[] fileList = ExternalDirectory.listFiles();
            if (fileList != null) {
                for (File file : fileList) {
                    if (file.getName().split("\\.")[1].equals("encraud")){
                        Record recording = new Record(file.getName());
                        if(recording.isPermanent){
                            if(recording.getStatus()<=0)
                                recording.removeRecording();
                            else if(recording.getStatus()*60 < min_time_to_remove)
                                min_time_to_remove = (int) recording.getStatus()*60;
                        }
                    }
                }
            }
        }
        return min_time_to_remove;
    }
}
