package com.ad.alphablackbox.logic.recorder

import android.media.AudioRecord
import android.util.Log
import com.ad.alphablackbox.logic.recorder.RecordingVariables.Companion.BufferElements2Rec
import com.ad.alphablackbox.logic.recorder.RecordingVariables.Companion.PCMStack
import com.ad.alphablackbox.logic.recorder.RecordingVariables.Companion.inDeque
import java.io.IOException
import java.util.*
import kotlin.experimental.and

class PCMGenerator {
    companion object {
        private fun shortToByte(sData: ShortArray): ByteArray {
            val arrSize = sData.size
            val bytes = ByteArray(arrSize * 2)
            for (i in 0 until arrSize) {
                bytes[i * 2] = (sData[i] and 0x00FF).toByte()
                bytes[i * 2 + 1] = (sData[i].toInt() shr 8).toByte()
                sData[i] = 0
            }
            return bytes
        }

        //generate pulse-code modulation(digitally represent sampled analog signals) in certain time periods(min)
        fun recordPCM(recorder: AudioRecord, timePeriod: Long) {
            val timer = Timer()
            var recordingCycle: Boolean
            while (RecordingVariables.isRecording) {
                Log.d("PCMGenerator", "New Recording Cycle")
                val buffer = ShortArray(BufferElements2Rec)
                val data = arrayListOf<Byte>()
                recordingCycle = true
                timer.schedule(object : TimerTask() {
                    override fun run() {
                        recordingCycle = false
                    }
                }, timePeriod * 1000)

                while (RecordingVariables.isRecording and recordingCycle) {
                    try {
                        recorder.read(buffer, 0, BufferElements2Rec)
                    } catch (e: IOException) {
                        Log.d("PCMGenerator Exception", e.toString())
                    }
                    try {
                        for (byte in shortToByte(buffer))
                            data.add(byte)
                    } catch (e: IOException) {
                        Log.d("PCMGenerator Exception", e.toString())
                    }
                }
                PCMStack.addLast(data)
                inDeque += 1
            }

        }
    }
}