package com.ad.alphablackbox.logic.recorder

class RecordingVariables {
    companion object {
        const val RECORDER_SAMPLE_RATE: Int = 8000
        const val RECORDER_CHANNELS: Int = android.media.AudioFormat.CHANNEL_IN_MONO
        const val RECORDER_AUDIO_ENCODING: Int = android.media.AudioFormat.ENCODING_PCM_16BIT
        const val BITS_PER_SAMPLE: Short = 16
        const val NUMBER_CHANNELS: Short = 1
        const val BYTE_RATE = RECORDER_SAMPLE_RATE * NUMBER_CHANNELS * 16 / 8
        const val BufferSizeInBytes = 512
        var BufferElements2Rec = 1024
        var isRecording = false
        var inDeque = 0
        var PCMStack = ArrayDeque<ArrayList<Byte>>()
        var WAVStack = ArrayDeque<ArrayList<Byte>>()
        var encryptedWAVStack = ArrayDeque<Pair<ArrayList<Byte>, ByteArray>>()
        var recordingsStack = ArrayDeque<String>()

        var timePeriod : Long = 30
        val defaultTimePeriod :Long = 60
    }
}