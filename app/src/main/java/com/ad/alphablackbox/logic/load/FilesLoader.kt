package com.ad.alphablackbox.logic.load

import java.io.File

class FilesLoader() {

    fun getAllFiles(path: String):Array<File>{
        val directory = File(path)
        return this.getAllFiles(directory)
    }

    fun getAllFiles(directory: File):Array<File>{
        var fileList = arrayOf<File>()
        directory.walk().forEach{
            fileList+=it
        }
        return fileList
    }
}