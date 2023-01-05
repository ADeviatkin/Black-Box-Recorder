package com.ad.alphablackbox.logic.recorder

import android.content.ServiceConnection


class Recorder (recordingAction: () -> Any, idleAction: () -> Any) {
    val ServiceClass get() = RecorderService::class.java
    val Connection get(): ServiceConnection = connection

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
        TODO()
    }
}