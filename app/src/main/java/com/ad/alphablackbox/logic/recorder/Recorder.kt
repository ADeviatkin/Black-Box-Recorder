package com.ad.alphablackbox.logic.recorder

import android.media.MediaRecorder
import java.io.File

class Recorder (){
    private var mediaRecorder: MediaRecorder? = null

    fun prepareRecordingCycle(recordPath: String) {
        mediaRecorder = MediaRecorder()
        mediaRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        mediaRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        mediaRecorder!!.setAudioChannels(1)
        mediaRecorder!!.setAudioSamplingRate(44100)
        mediaRecorder!!.setAudioEncodingBitRate(192000)
        mediaRecorder!!.setOutputFile(recordPath)
    }

    private fun startRecordingCycle() {
        while (true){ // !service is cancelled

        }
    }

    private fun saveTemporaryRecord() {
        mediaRecorder!!.stop()
        mediaRecorder!!.release()
        mediaRecorder = null
    }
}