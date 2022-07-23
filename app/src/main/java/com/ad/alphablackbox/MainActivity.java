package com.ad.alphablackbox;

import android.Manifest;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {
    private Button startRecordingButton, saveLastRecordingsButton, stopRecordingButton;
    BackgroundMediaService.LocalBinder binder;
    private BackgroundMediaService mService;
    private boolean mBound = false;
    private Intent mIntent;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            binder = (BackgroundMediaService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
            System.out.println("connected to service");
            if (!mService.isCancelled) {
                startRecordingButton.setEnabled(false);
            }
            else
            {
                saveLastRecordingsButton.setEnabled(false);
                stopRecordingButton.setEnabled(false);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
            System.out.println("disconnected with service");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startRecordingButton = (Button) findViewById(R.id.start_recording_button);
        saveLastRecordingsButton = (Button) findViewById(R.id.save_last_recordings_button);
        stopRecordingButton = (Button) findViewById(R.id.stop_recording_button);
        mIntent = new Intent(MainActivity.this, BackgroundMediaService.class);
        bindService(mIntent, connection, Context.BIND_AUTO_CREATE);
        startRecordingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckPermissions()) {
                    startRecordingButton.setEnabled(false);
                    saveLastRecordingsButton.setEnabled(true);
                    stopRecordingButton.setEnabled(true);
                    if(!mService.isInstanceCreated())
                    {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            startForegroundService(mIntent);
                        } else {
                            startService(mIntent);
                        }
                    }
                    if (mBound) {
                        mService.setRecordsDirectory(String.valueOf(getApplicationContext().getExternalFilesDir(null)));
                        mService.startForeground();
                        mService.startRecordingThread();
                    }
                } else {
                    RequestPermissions();
                }
            }
        });

        saveLastRecordingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mService.savePermanentRecord();
            }
        });
        stopRecordingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mService.stopRecordingCycle();
                startRecordingButton.setEnabled(true);
                saveLastRecordingsButton.setEnabled(false);
                stopRecordingButton.setEnabled(false);
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    protected void onStop() {
        super.onStop();
        //unbindService(connection);
        //mService.onDestroy();
        //mBound = false;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //mService.onDestroy();
        unbindService(connection);
    }

        public boolean CheckPermissions() {
        return ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void RequestPermissions() {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
    }

}