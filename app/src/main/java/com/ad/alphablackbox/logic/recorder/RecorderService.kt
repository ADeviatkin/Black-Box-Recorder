package com.ad.alphablackbox.logic.recorder

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder

class RecorderService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    inner class LocalBinder : Binder() {
        val service: RecorderService
            get() = this@RecorderService
    }
}