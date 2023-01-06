package com.ad.alphablackbox.logic.service

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log

open class RecorderServiceConnection() : ServiceConnection {
    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        Log.d("Service", "Connected")
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        Log.d("Service", "Disconnected")
    }
}