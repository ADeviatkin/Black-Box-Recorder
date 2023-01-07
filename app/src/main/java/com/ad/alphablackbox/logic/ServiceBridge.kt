package com.ad.alphablackbox.logic

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.ad.alphablackbox.logic.background.Service
import com.ad.alphablackbox.logic.background.Connection

class ServiceBridge(activity: Context){
    private var activity: Context = activity
    private lateinit var recorderIntent: Intent
    private lateinit var connection: Connection

    fun connect(){
        Log.d("App", "Bind to Service")
        recorderIntent = Intent(activity, Service::class.java)
        connection = Connection()
        activity.bindService(recorderIntent, connection, AppCompatActivity.BIND_AUTO_CREATE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            activity.startForegroundService(recorderIntent)
        }
    }
    fun disconnect() {
        Log.d("App", "Unbind than disconnect with Service")
        activity.stopService(recorderIntent)
        activity.unbindService(connection)
    }
}