package com.ad.alphablackbox.logic.recorder

class HeaderConstructor {
    companion object {
        // Constructs header for wav file format
        private fun wavFileHeader(): ByteArray {
            val headerSize = 44
            val header = ByteArray(headerSize)

            header[0] = 'R'.code.toByte() // RIFF/WAVE header
            header[1] = 'I'.code.toByte()
            header[2] = 'F'.code.toByte()
            header[3] = 'F'.code.toByte()

            header[4] = (0 and 0xff).toByte() // Size of the overall file, 0 because unknown
            header[5] = (0 shr 8 and 0xff).toByte()
            header[6] = (0 shr 16 and 0xff).toByte()
            header[7] = (0 shr 24 and 0xff).toByte()

            header[8] = 'W'.code.toByte()
            header[9] = 'A'.code.toByte()
            header[10] = 'V'.code.toByte()
            header[11] = 'E'.code.toByte()

            header[12] = 'f'.code.toByte() // 'fmt ' chunk
            header[13] = 'm'.code.toByte()
            header[14] = 't'.code.toByte()
            header[15] = ' '.code.toByte()

            header[16] = 16 // Length of format data
            header[17] = 0
            header[18] = 0
            header[19] = 0

            header[20] = 1 // Type of format (1 is PCM)
            header[21] = 0

            header[22] = RecordingVariables.NUMBER_CHANNELS.toByte()
            header[23] = 0

            header[24] =
                    (RecordingVariables.RECORDER_SAMPLE_RATE and 0xff).toByte() // Sampling rate
            header[25] = (RecordingVariables.RECORDER_SAMPLE_RATE shr 8 and 0xff).toByte()
            header[26] = (RecordingVariables.RECORDER_SAMPLE_RATE shr 16 and 0xff).toByte()
            header[27] = (RecordingVariables.RECORDER_SAMPLE_RATE shr 24 and 0xff).toByte()

            header[28] =
                    (RecordingVariables.BYTE_RATE and 0xff).toByte() // Byte rate = (Sample Rate * BitsPerSample * Channels) / 8
            header[29] = (RecordingVariables.BYTE_RATE shr 8 and 0xff).toByte()
            header[30] = (RecordingVariables.BYTE_RATE shr 16 and 0xff).toByte()
            header[31] = (RecordingVariables.BYTE_RATE shr 24 and 0xff).toByte()

            header[32] =
                    (RecordingVariables.NUMBER_CHANNELS * RecordingVariables.BITS_PER_SAMPLE / 8).toByte() //  16 Bits stereo
            header[33] = 0

            header[34] = RecordingVariables.BITS_PER_SAMPLE.toByte() // Bits per sample
            header[35] = 0

            header[36] = 'd'.code.toByte()
            header[37] = 'a'.code.toByte()
            header[38] = 't'.code.toByte()
            header[39] = 'a'.code.toByte()

            header[40] = (0 and 0xff).toByte() // Size of the data section.
            header[41] = (0 shr 8 and 0xff).toByte()
            header[42] = (0 shr 16 and 0xff).toByte()
            header[43] = (0 shr 24 and 0xff).toByte()

            return header
        }

        private fun updateHeaderInformation(data: ArrayList<Byte>) {
            val fileSize = data.size
            val contentSize = fileSize - 44

            data[4] = (fileSize and 0xff).toByte() // Size of the overall file
            data[5] = (fileSize shr 8 and 0xff).toByte()
            data[6] = (fileSize shr 16 and 0xff).toByte()
            data[7] = (fileSize shr 24 and 0xff).toByte()

            data[40] = (contentSize and 0xff).toByte() // Size of the data section.
            data[41] = (contentSize shr 8 and 0xff).toByte()
            data[42] = (contentSize shr 16 and 0xff).toByte()
            data[43] = (contentSize shr 24 and 0xff).toByte()
        }

        fun addHeaderToPCM(data: ArrayList<Byte>): ArrayList<Byte> {
            val output = arrayListOf<Byte>()
            for (byte in HeaderConstructor.wavFileHeader()) {
                output.add(byte)
            }
            for (byte in data) {
                output.add(byte)
            }
            HeaderConstructor.updateHeaderInformation(output)
            return output
        }
    }
}