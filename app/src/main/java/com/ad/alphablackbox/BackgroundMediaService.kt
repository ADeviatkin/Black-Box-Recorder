package com.ad.alphablackbox

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.MediaRecorder
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class BackgroundMediaService : Service() {
    companion object{
        private var instance: BackgroundMediaService? = null
        @JvmField val ID_SERVICE = 101
    }
    private var mThread: Thread? = null
    private var mRecorder: MediaRecorder? = null
    private val temporaryRecordList: MutableList<String> = ArrayList<String>()
    private val maxRecordListSize = 5
    private var recordsDirectory: String? = null
    private var recordPath: String? = null
    private val mBinder: IBinder = LocalBinder()
    private val recordingFormat = ".wav"
    private val recordingTimeInterval = 1000 * 60 * 10
    public var isCancelled = true

    inner class LocalBinder : Binder() {
        val service: BackgroundMediaService
            get() = this@BackgroundMediaService
    }

    fun isInstanceCreated(): Boolean {
        return instance != null
    }

    override fun onBind(intent: Intent?): IBinder? {
        return mBinder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(this, "Service started by user.", Toast.LENGTH_LONG).show()
        instance = this
        return super.onStartCommand(intent, flags, startId)
    }

    fun startForeground() {
        val NOTIFICATION_CHANNEL_ID = "com.AD.blackboxnotification"
        val channelName = "Black Box"
        var chan: NotificationChannel? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            chan = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                channelName,
                NotificationManager.IMPORTANCE_NONE
            )
            chan.lightColor = Color.BLUE
            chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            val manager = (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?)!!
            manager.createNotificationChannel(chan)
        }
        val notificationBuilder = NotificationCompat.Builder(
            this,
            NOTIFICATION_CHANNEL_ID
        )
        val notification: Notification = notificationBuilder.setOngoing(true)
            .setSmallIcon(R.drawable.icon_1)
            .setContentTitle("App is running in background")
            .setPriority(NotificationManager.IMPORTANCE_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
        startForeground(ID_SERVICE, notification)
    }

    fun startRecordingThread() {
        isCancelled = false
        mThread = object : Thread() {
            override fun run() {
                while (true) {
                    try {
                        startRecordingCycle()
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
            }
        }
        mThread!!.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        instance = null
        Toast.makeText(this, "Service destroyed by user.", Toast.LENGTH_LONG).show()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stopForeground(Service.STOP_FOREGROUND_REMOVE)
        }
    }

    @Throws(InterruptedException::class)
    fun prepareRecordingCycle() {
        mRecorder = MediaRecorder()
        mRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
        mRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        mRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        mRecorder!!.setAudioChannels(1)
        mRecorder!!.setAudioSamplingRate(44100)
        mRecorder!!.setAudioEncodingBitRate(192000)
        recordPath = createRecordPath()
        temporaryRecordList.add(recordPath ?: "")
        mRecorder!!.setOutputFile(recordPath)
    }

    @Throws(InterruptedException::class)
    private fun startRecordingCycle() {
        while (!isCancelled) {
            prepareRecordingCycle()
            if (temporaryRecordList.size > maxRecordListSize) {
                deleteTemporaryRecord()
            }
            try {
                mRecorder!!.prepare()
                mRecorder!!.start()
                Thread.sleep(recordingTimeInterval.toLong())
                saveTemporaryRecord()
            } catch (e: IOException) {
                Log.e("AudioRecording", "prepare() or start() failed")
            }
        }
    }

    fun stopRecordingCycle() {
        isCancelled = true
        mRecorder = null
    }

    fun setRecordsDirectory(directory: String?) {
        recordsDirectory = directory
    }

    private fun createRecordPath(): String {
        val date = Date()
        val formatter = SimpleDateFormat("HH.mm.ss dd-MM-yyyy")
        return recordsDirectory + File.separator + formatter.format(date) + recordingFormat
    }

    private fun saveTemporaryRecord() {
        mRecorder!!.stop()
        mRecorder!!.release()
        mRecorder = null
    }

    private fun deleteTemporaryRecord() {
        val file = File(temporaryRecordList[0])
        temporaryRecordList.removeAt(0)
        if (file != null) {
            file.delete()
        }
    }

    public fun savePermanentRecord() {
        if (temporaryRecordList.size > 1) {
            temporaryRecordList.removeAt(temporaryRecordList.size - 1)
        }
    }
}