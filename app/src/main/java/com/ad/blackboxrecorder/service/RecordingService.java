package com.ad.blackboxrecorder.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.ad.blackboxrecorder.R;
import com.ad.blackboxrecorder.activities.Main.model.recording.RecordingHandler;


public class RecordingService extends Service {
    public static final int disabled = 0;
    public static final int recording = 2;
    public static final int loading = 4;
    public static int status = 0;
    private final IBinder binder = new LocalBinder();
    private final int notificationId = 1;
    private final String notificationChannelId = "BBRecorder_Channel_Id";
    private final String notificationChannelName = "BBRecorder_Channel";
    private final String notificationContentTitle = "BBRecorder - Background";
    private final String notificationContentText = "App is running in background and record sound 24/7";

    public class LocalBinder extends Binder {
        public Service getService() {
            return RecordingService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("Service", "Created");
        status = loading;
    }
    RecordingHandler RecordingHandler;
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Service", "Destroyed");
        status = disabled;
        RecordingHandler.stop();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();
        Notification notification = createNotification();
        startForeground(notificationId, notification);
        RecordingHandler = new RecordingHandler();
        RecordingHandler.start();
        status = recording;
        return super.onStartCommand(intent, flags, startId);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(notificationChannelId, notificationChannelName, importance);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private Notification createNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, notificationChannelId)
                .setSmallIcon(R.drawable.record_unclicked)
                .setContentTitle(notificationContentTitle)
                .setContentText(notificationContentText)
                .setAutoCancel(true);
        return builder.build();
    }
}