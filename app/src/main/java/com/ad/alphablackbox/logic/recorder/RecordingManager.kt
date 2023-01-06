package com.ad.alphablackbox.logic.recorder

import android.content.ServiceConnection
import com.ad.alphablackbox.logic.service.RecorderService
import com.ad.alphablackbox.logic.service.RecorderServiceConnection


class RecordingManager () {
    val ServiceClass get() = RecorderService::class.java
    val Connection get(): ServiceConnection = connection
    val temporaryRecordList: MutableList<String> = ArrayList()

    private val service = RecorderService()
    private val connection = RecorderServiceConnection()

    fun startRecording() {

    }

    fun stopRecording() {

    }

    fun savePermanentRecord() {

    }

    fun setRecordsDirectory(path: String) {

    }

}