package com.ad.alphablackbox.logic.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log

class RecorderService : Service() {
    private val binder: IBinder = LocalBinder()

    inner class LocalBinder : Binder() {
        fun getService(): RecorderService {
            return this@RecorderService
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("Service", "Created")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Service", "Destroyed")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }


}