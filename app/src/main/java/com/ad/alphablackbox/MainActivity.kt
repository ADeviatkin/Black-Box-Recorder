package com.ad.alphablackbox

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ad.alphablackbox.logic.Permissions.Companion.ensurePermissions
import com.ad.alphablackbox.logic.ServiceBridge


class MainActivity : AppCompatActivity() {
    private lateinit var bridgeToRecorderService: ServiceBridge

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ensurePermissions(this, applicationContext)
        bridgeToRecorderService = ServiceBridge(applicationContext)
        bridgeToRecorderService.connect()
    }

    override fun onDestroy() {
        super.onDestroy()
        //bridgeToRecorderService.disconnect()
    }
}

