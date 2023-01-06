package com.ad.alphablackbox.logic.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.ad.alphablackbox.R

class RecorderService : Service() {
    private val binder: IBinder = LocalBinder()
    private val notificationId = 1
    private val notificationChannelId = "my_channel_id"
    private val notificationChannelName = "My Channel"
    private val notificationContentTitle = "Background Recorder"
    private val notificationContentText = "App is running in background and record sound 24/7"

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
        createNotificationChannel()
        val notification = createNotification()
        startForeground(notificationId, notification)
        return super.onStartCommand(intent, flags, startId)
    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(notificationChannelId, notificationChannelName, importance)
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun createNotification(): Notification {
        val builder = NotificationCompat.Builder(this, notificationChannelId)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle(notificationContentTitle)
                .setContentText(notificationContentText)
                .setAutoCancel(true)
        return builder.build()
    }

}