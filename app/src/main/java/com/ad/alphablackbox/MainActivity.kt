package com.ad.alphablackbox

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.ad.alphablackbox.logic.Permissions.Companion.ensurePermissions
import com.ad.alphablackbox.logic.ServiceBridge.Companion.connectToService
import com.ad.alphablackbox.logic.recorder.RecordingManager


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ensurePermissions(this, applicationContext)
        Log.d("App", "Try to connect")
        connectToService(applicationContext)
    }
}

