package com.ad.alphablackbox;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaRecorder;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BackgroundMediaService extends Service {
    private static BackgroundMediaService instance = null;
    private static final int ID_SERVICE = 101;
    private Thread mThread;
    private MediaRecorder mRecorder;
    private List temporaryRecordList = new ArrayList();
    private int maxRecordListSize = 5;
    private String recordsDirectory, recordPath;
    private final IBinder mBinder = new LocalBinder();
    private String recordingFormat = ".wav";
    private int recordingTimeInterval = 1000 * 60 * 10;
    public boolean isCancelled = true;

    public class LocalBinder extends Binder {
        BackgroundMediaService getService() {
            return BackgroundMediaService.this;
        }
    }

    public static boolean isInstanceCreated() {
        return instance != null;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service started by user.", Toast.LENGTH_LONG).show();
        instance = this;
        return super.onStartCommand(intent, flags, startId);
    }

    public void startForeground() {
        String NOTIFICATION_CHANNEL_ID = "com.AD.blackboxnotification";
        String channelName = "Black Box";
        NotificationChannel chan = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
            chan.setLightColor(Color.BLUE);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.icon_1)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(ID_SERVICE, notification);
    }

    public void startRecordingThread() {
        isCancelled = false;
        mThread = new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        startRecordingCycle();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        mThread.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        instance = null;
        Toast.makeText(this, "Service destroyed by user.", Toast.LENGTH_LONG).show();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stopForeground(Service.STOP_FOREGROUND_REMOVE);
        }
    }

    protected void prepareRecordingCycle() throws InterruptedException {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mRecorder.setAudioChannels(1);
        mRecorder.setAudioSamplingRate(44100);
        mRecorder.setAudioEncodingBitRate(192000);
        recordPath = createRecordPath();
        temporaryRecordList.add(recordPath);
        mRecorder.setOutputFile(recordPath);
    }

    protected void startRecordingCycle() throws InterruptedException {
        while (!isCancelled) {
            prepareRecordingCycle();
            if (temporaryRecordList.size() > maxRecordListSize) {
                deleteTemporaryRecord();
            }
            try {
                mRecorder.prepare();
                mRecorder.start();
                Thread.sleep(recordingTimeInterval);
                saveTemporaryRecord();
            } catch (IOException e) {
                Log.e("AudioRecording", "prepare() or start() failed");
            }

        }
    }

    public void stopRecordingCycle() {
        isCancelled = true;
        mRecorder = null;
    }

    public void setRecordsDirectory(String directory) {
        recordsDirectory = directory;
    }

    private String createRecordPath() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("HH.mm.ss dd-MM-yyyy");
        return recordsDirectory + File.separator + formatter.format(date) + recordingFormat;
    }

    private void saveTemporaryRecord() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

    private void deleteTemporaryRecord() {
        File file = new File((String) temporaryRecordList.get(0));
        temporaryRecordList.remove(0);
        if (file != null) {
            file.delete();
        }
    }

    public void savePermanentRecord() {
        if(temporaryRecordList.size() > 1)
        {
            temporaryRecordList.remove(temporaryRecordList.size() - 1);
        }
    }

}