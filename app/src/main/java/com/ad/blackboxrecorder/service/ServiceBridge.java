package com.ad.blackboxrecorder.service;

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
    private Context activity;
    private Intent recorderIntent;
    private Connection connection;

    public ServiceBridge(Context activity) {
        this.activity = activity;
    }

    public void connect() {
        Log.d("App", "Bind to Service");
        recorderIntent = new Intent(activity, RecordingService.class);
        connection = new Connection();
        activity.bindService(recorderIntent, connection, AppCompatActivity.BIND_AUTO_CREATE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            activity.startForegroundService(recorderIntent);
        }
    }

    public void disconnect() {
        Log.d("App", "Unbind then disconnect with Service");
        activity.stopService(recorderIntent);
        activity.unbindService(connection);
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
}