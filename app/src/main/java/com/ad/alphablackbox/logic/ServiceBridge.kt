package com.ad.alphablackbox.logic

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.ad.alphablackbox.logic.service.RecorderService
import com.ad.alphablackbox.logic.service.RecorderServiceConnection

class ServiceBridge {
    companion object{
        fun connectToService(mainActivity: Context){
            Log.d("App", "Bind from app")
            val recorderIntent = Intent(mainActivity, RecorderService::class.java)
            val connection = RecorderServiceConnection()
            mainActivity.bindService(recorderIntent, connection, AppCompatActivity.BIND_AUTO_CREATE)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mainActivity.startForegroundService(recorderIntent)
            }
        }
    }
}