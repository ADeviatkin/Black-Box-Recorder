package com.ad.alphablackbox.logic

import com.ad.alphablackbox.logic.recorder.RecordingVariables.Companion.recordingsStack
import java.io.*

class FileExplorer {
    companion object {
        fun writeData(data: ArrayList<Byte>, path: String, _filename: String? = null) {
            val filename = _filename ?: "recording-${System.currentTimeMillis()}.encraud"
            val path = "$path/$filename"
            recordingsStack.addFirst(path)

            var os: FileOutputStream? = null
            try {
                os = FileOutputStream(path)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }

            os?.write(data.toByteArray())
            try {
                os?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        fun readData(file: String): ArrayList<Byte> {
            // Open the file for reading
            val fileInputStream = FileInputStream("$file")
            val bufferedInputStream = BufferedInputStream(fileInputStream)

            // Read the data and write it to a ByteArrayOutputStream
            val outputStream = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var read = bufferedInputStream.read(buffer)
            while (read != -1) {
                outputStream.write(buffer, 0, read)
                read = bufferedInputStream.read(buffer)
            }

            // Close the BufferedInputStream
            bufferedInputStream.close()

            return outputStream.toByteArray().toCollection(ArrayList())
        }
    }
}