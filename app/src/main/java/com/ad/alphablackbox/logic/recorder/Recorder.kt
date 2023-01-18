package com.ad.alphablackbox.logic.recorder

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log
import com.ad.alphablackbox.logic.cryptography.UCipher
import com.ad.alphablackbox.logic.FileExplorer.Companion.writeData
import com.ad.alphablackbox.logic.cryptography.UCipher.Companion.encrypt
import com.ad.alphablackbox.logic.recorder.HeaderConstructor.Companion.addHeaderToPCM
import com.ad.alphablackbox.logic.recorder.RecordingVariables.Companion.PCMStack
import com.ad.alphablackbox.logic.recorder.RecordingVariables.Companion.WAVStack
import com.ad.alphablackbox.logic.recorder.RecordingVariables.Companion.encryptedWAVStack
import kotlin.concurrent.thread


class Recorder {

    private var recorder: AudioRecord? = null
    private val path: String
    private var recordingThread: Thread? = null
    private var convertingThread: Thread? = null
    private var writingThread: Thread? = null
    private var encryptingThread: Thread? = null
    private var mainContext: Context? = null

    constructor(context: Context, path: String) {
        this.mainContext =context
        this.path = path
    }

    @SuppressLint("MissingPermission")
    fun startRecording() {

        recorder = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                RecordingVariables.RECORDER_SAMPLE_RATE, RecordingVariables.RECORDER_CHANNELS,
                RecordingVariables.RECORDER_AUDIO_ENCODING, RecordingVariables.BufferSizeInBytes
        )

        recorder?.startRecording()
        RecordingVariables.isRecording = true

        //records the audio
        recordingThread = thread(true) {
            PCMGenerator.recordPCM(recorder!!, 10)
        }

        //converts audio into wav format by adding special header
        convertingThread = thread(true) {
            while (RecordingVariables.isRecording || RecordingVariables.inDeque != 0 || !PCMStack.isEmpty()) {
                if (!PCMStack.isEmpty()) {
                    Log.d("TAAAAG", "Done convertingThread")
                    WAVStack.addLast(addHeaderToPCM(PCMStack.removeFirst()))
                }
            }
        }

        //encrypt audio
        encryptingThread = thread(true) {
            while (RecordingVariables.isRecording || RecordingVariables.inDeque != 0|| !WAVStack.isEmpty()) {
                if (!WAVStack.isEmpty()) {
                    Log.d("TAAAAG", "Done encryptingThread")
                    encryptedWAVStack.addLast(encrypt(WAVStack.removeLast()))
                }
            }
        }

        //writes encrypted audio to file
        writingThread = thread(true) {
            while (RecordingVariables.isRecording || RecordingVariables.inDeque != 0 || !encryptedWAVStack.isEmpty()) {
                if (!encryptedWAVStack.isEmpty()) {
                    Log.d("TAAAAG", "Done writingThread")
                    writeData(encryptedWAVStack.removeFirst(), this.path!!)
                    RecordingVariables.inDeque -= 1
                }
            }
        }
    }

    fun stopRecording() {
        recorder?.run {
            RecordingVariables.isRecording = false;
            stop()
            release()
            recordingThread = null
            recorder = null
        }
    }

}