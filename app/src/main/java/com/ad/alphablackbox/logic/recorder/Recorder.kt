package com.ad.alphablackbox.logic.recorder

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioRecord
import android.media.MediaRecorder
import com.ad.alphablackbox.logic.FileExplorer.Companion.writeData
import com.ad.alphablackbox.logic.recorder.HeaderConstructor.Companion.addHeaderToPCM
import com.ad.alphablackbox.logic.recorder.RecordingVariables.Companion.PCMStack
import com.ad.alphablackbox.logic.recorder.RecordingVariables.Companion.WAVStack
import kotlin.concurrent.thread


class Recorder {
    //var cipherAES: UCipher? = null
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
        // create Cipher with AES encrypting
        //this.cipherAES = UCipher("AES")

        recorder = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                RecordingVariables.RECORDER_SAMPLE_RATE, RecordingVariables.RECORDER_CHANNELS,
                RecordingVariables.RECORDER_AUDIO_ENCODING, RecordingVariables.BufferSizeInBytes
        )

        recorder?.startRecording()
        RecordingVariables.isRecording = true

        //records the audio
        recordingThread = thread(true) {
            PCMGenerator.recordPCM(recorder!!, 1)
        }

        //converts audio into wav format by adding special header
        convertingThread = thread(true) {
            while (RecordingVariables.isRecording || RecordingVariables.inDeque != 0 || !PCMStack.isEmpty()) {
                if (!PCMStack.isEmpty()) {
                    WAVStack.addLast(addHeaderToPCM(PCMStack.removeFirst()))
                }
            }
        }

        """-
        //encrypt audio
        encryptingThread = thread(true) {
            while (RecordingVariables.isRecording || RecordingVariables.inDeque != 0) {
                if (!WAVStack.isEmpty()) {
                    encryptedWAVStack.addLast(this.cipherAES!!.encrypt(WAVStack.removeLast()))
                }
            }
        }
        """

        //writes encrypted audio to file
        writingThread = thread(true) {
            while (RecordingVariables.isRecording || RecordingVariables.inDeque != 0 || !WAVStack.isEmpty()) {
                if (!WAVStack.isEmpty()) {
                    writeData(WAVStack.removeFirst(), this.path!!)
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