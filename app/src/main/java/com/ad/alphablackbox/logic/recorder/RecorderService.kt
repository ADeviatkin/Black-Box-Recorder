package com.ad.alphablackbox.logic.recorder

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import com.ad.alphablackbox.logic.ToastUtils

class RecorderService : Service() {
    private val binder: IBinder = LocalBinder()
    companion object{
        private var instance: RecorderService? = null
        @JvmField val ID_SERVICE = 101
    }

    inner class LocalBinder : Binder() {
        val service: RecorderService
            get() = this@RecorderService
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        ToastUtils.makeStartText(this)
        instance = this
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        instance = null

        ToastUtils.makeDestroyText(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stopForeground(Service.STOP_FOREGROUND_REMOVE)
        }
    }

    fun isInstanceCreated(): Boolean{
        return instance != null
    }


}