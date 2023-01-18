package com.ad.alphablackbox.logic

import android.util.Log
import com.ad.alphablackbox.logic.cryptography.UCipher
import com.ad.alphablackbox.logic.recorder.RecordingVariables.Companion.recordingsStack
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import java.io.*

class FileExplorer {

    data class DataPackage(
        @SerializedName("Data") val data: ArrayList<Byte>,
        @SerializedName("Iv") val iv: ByteArray
    )

    companion object {
        private val gson = Gson()
        fun writeData(data: Pair<ArrayList<Byte>, ByteArray>, path: String, _filename: String? = null) {
            val filename = _filename ?: "recording-${System.currentTimeMillis()}.encraud"
            val path = "$path/$filename"
            recordingsStack.addFirst(path)

            var os: FileOutputStream? = null
            try {
                os = FileOutputStream(path)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }

            var (pcmData, iv) = data
            val invoiceAdditionalAttribute = DataPackage(pcmData, iv)

            val jsonString = gson.toJson(invoiceAdditionalAttribute)  // json string

            os?.write(jsonString.toByteArray())
            try {
                os?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }

        fun readData(file: String): Pair<ArrayList<Byte>, ByteArray> {

            // Open file for reading
            Log.d("TAG", "$file")
            val fileInputStream = FileInputStream("$file")
            val bufferedInputStream = BufferedInputStream(fileInputStream)

            // Read data
            val outputStream = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var read = bufferedInputStream.read(buffer)
            while (read != -1) {
                outputStream.write(buffer, 0, read)
                read = bufferedInputStream.read(buffer)
            }
            val formattedData = gson.fromJson(outputStream.toString(), DataPackage::class.java)

            // Close the BufferedInputStream
            bufferedInputStream.close()

            return Pair(formattedData.data, formattedData.iv)
        }

        fun getAllFiles(directory: File):Array<File>{
            var fileList = arrayOf<File>()
            directory.walk().forEach{
                fileList += it
            }
            return fileList
        }

        fun getFileNames(path: String):Array<String>{
            val directory = File(path)
            var fileList = arrayOf<String>()
            directory.walk().forEach{
                fileList += it.name
            }
            return fileList
        }

    }
}