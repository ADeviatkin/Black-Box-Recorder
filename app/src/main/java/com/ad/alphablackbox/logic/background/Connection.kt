package com.ad.alphablackbox.logic.background

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log

class Connection() : ServiceConnection {

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        Log.d("Service", "Connected")
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        Log.d("Service", "Disconnected")
    }

}