package com.ad.alphablackbox;

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {
    private var startRecordingButton: Button? = null
    private var saveLastRecordingsButton: Button? = null
    private var stopRecordingButton: Button? = null
    var binder: BackgroundMediaService.LocalBinder? = null
    private var mService: BackgroundMediaService? = null
    private var mBound = false
    private var mIntent: Intent? = null
    private val connection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            binder = service as BackgroundMediaService.LocalBinder
            mService = binder!!.service
            mBound = true
            println("connected to service")
            if (mService!!.isCancelled) {
                startRecordingButton!!.isEnabled = false
            } else {
                saveLastRecordingsButton!!.isEnabled = false
                stopRecordingButton!!.isEnabled = false
            }
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
            println("disconnected with service")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startRecordingButton = findViewById<View>(R.id.start_recording_button) as Button
        saveLastRecordingsButton = findViewById<View>(R.id.save_last_recordings_button) as Button
        stopRecordingButton = findViewById<View>(R.id.stop_recording_button) as Button
        mIntent = Intent(this@MainActivity, BackgroundMediaService::class.java)
        bindService(mIntent, connection, BIND_AUTO_CREATE)
        startRecordingButton!!.setOnClickListener {
            if (checkPermissions()) {
                startRecordingButton!!.isEnabled = false
                saveLastRecordingsButton!!.isEnabled = true
                stopRecordingButton!!.isEnabled = true
                if (!mService!!.isInstanceCreated()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(mIntent)
                    } else {
                        startService(mIntent)
                    }
                }
                if (mBound) {
                    mService!!.setRecordsDirectory(
                        applicationContext.getExternalFilesDir(null).toString()
                    )
                    mService!!.startForeground()
                    mService!!.startRecordingThread()
                }
            } else {
                requestPermissions()
            }
        }
        saveLastRecordingsButton!!.setOnClickListener { mService!!.savePermanentRecord() }
        stopRecordingButton!!.setOnClickListener {
            mService!!.stopRecordingCycle()
            startRecordingButton!!.isEnabled = true
            saveLastRecordingsButton!!.isEnabled = false
            stopRecordingButton!!.isEnabled = false
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
        //unbindService(connection);
        //mService.onDestroy();
        //mBound = false;
    }

    override fun onDestroy() {
        super.onDestroy()
        //mService.onDestroy();
        unbindService(connection)
    }

    private fun checkPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this@MainActivity,
            arrayOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            123
        )
    }
}

