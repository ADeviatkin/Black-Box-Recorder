package com.ad.alphablackbox.logic.recorder

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder

class RecorderServiceConnection(recordingAction: () -> Any, idleAction: () -> Any) : ServiceConnection {
    private val changeUiOnRecording = recordingAction
    private val changeUiOnIdle = idleAction


    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val recorderService = (service as RecorderService.LocalBinder).service

        //TODO: if statement needed
        changeUiOnRecording()
        changeUiOnIdle()
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        TODO("Not yet implemented")
    }
}