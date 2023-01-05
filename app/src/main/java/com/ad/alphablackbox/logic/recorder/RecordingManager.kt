package com.ad.alphablackbox.logic.recorder

import android.content.ServiceConnection


class RecordingManager (recordingAction: () -> Any, idleAction: () -> Any) {
    val ServiceClass get() = RecorderService::class.java
    val Connection get(): ServiceConnection = connection
    val temporaryRecordList: MutableList<String> = ArrayList()

    private val service = RecorderService()
    private val connection = RecorderServiceConnection(recordingAction, idleAction)

    fun startRecording() {

    }

    fun stopRecording() {

    }

    fun savePermanentRecord() {

    }

    fun setRecordsDirectory(path: String) {

    }

    fun serviceExists(): Boolean{
        return service.isInstanceCreated()
    }
}