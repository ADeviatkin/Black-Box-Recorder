package com.ad.blackboxrecorder.service;

import static android.app.PendingIntent.getActivity;
import static androidx.core.content.ContextCompat.getSystemService;

import static com.ad.blackboxrecorder.gui.ButtonManager.changeRecordButtonStatus;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.ad.blackboxrecorder.service.RecordingService;

public class ServiceBridge {
    public static boolean isServiceRunning = false;
    private Context activity;
    private Intent recorderIntent;
    private Connection connection;

    public ServiceBridge(Context activity) {
        this.activity = activity;
        if (isServiceRunning(RecordingService.class)){
            isServiceRunning = true;
            changeRecordButtonStatus(true);
            Log.d("App", "Already");
        }
    }

    public void connect() {
        Log.d("App", "Bind to Service");
        recorderIntent = new Intent(activity, RecordingService.class);
        connection = new Connection();
        activity.bindService(recorderIntent, connection, AppCompatActivity.BIND_AUTO_CREATE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            activity.startForegroundService(recorderIntent);
        }
        if (isServiceRunning(RecordingService.class)){
            isServiceRunning = true;
        }
    }

    public void disconnect() {
        Log.d("App", "Unbind then disconnect with Service");
        recorderIntent = new Intent(activity, RecordingService.class);
        activity.stopService(recorderIntent);
        //activity.unbindService(connection);
        if (!isServiceRunning(RecordingService.class)){
            isServiceRunning = false;
        }
    }

    private class Connection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("Service", "Connected");
            // Implement your service connection logic here
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("Service", "Disconnected");
            // Implement your service disconnection logic here
        }
    }
    @SuppressLint("Deprecated")
    public <T> boolean isServiceRunning(Class<T> serviceClass) {
        ActivityManager manager = (ActivityManager) this.activity.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}