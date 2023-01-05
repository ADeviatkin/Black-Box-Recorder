package com.ad.alphablackbox

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.ad.alphablackbox.logic.Permissions
import com.ad.alphablackbox.logic.recorder.RecordingManager


class MainActivity : AppCompatActivity() {
    private var startRecordingButton: Button? = null
    private var saveLastRecordingsButton: Button? = null
    private var stopRecordingButton: Button? = null

    private val recordingManager = RecordingManager(
        recordingAction = ::changeButtonsToRecording,
        idleAction = ::changeButtonsToStop
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initButtons()

        ensurePermissions()
//        connectToRecorderService()
    }

    fun startRecording() {
        recordingManager.startRecording()
        changeButtonsToRecording()
    }

    fun stopRecording() {
        recordingManager.stopRecording()
        changeButtonsToStop()
    }

    fun saveLastRecordings() {
        recordingManager.savePermanentRecord()
    }

    private fun initButtons() {
        startRecordingButton = findViewById(R.id.start_recording_button)
        saveLastRecordingsButton = findViewById(R.id.save_last_recordings_button)
        stopRecordingButton = findViewById(R.id.stop_recording_button)
    }

    private fun changeButtonsToRecording() {
        startRecordingButton!!.isEnabled = false
    }

    private fun changeButtonsToStop() {
        startRecordingButton!!.isEnabled = false
        stopRecordingButton!!.isEnabled = false
    }

    private fun ensurePermissions() {
        if (!Permissions.allAllowed(applicationContext)){
            Permissions.requestAll(this@MainActivity)
        }
    }

    private fun connectToRecorderService(){
        val recorderIntent = Intent(this@MainActivity, recordingManager.ServiceClass)
        bindService(recorderIntent, recordingManager.Connection, BIND_AUTO_CREATE)
        if (!recordingManager.serviceExists()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(recorderIntent)
            } else {
                startService(recorderIntent)
            }
        }
    }
}

