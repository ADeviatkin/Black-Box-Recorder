package com.ad.alphablackbox.logic.player

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.SeekBar
import com.ad.alphablackbox.MainActivity.Companion.recordsDir
import com.ad.alphablackbox.R
import com.ad.alphablackbox.logic.FileExplorer
import com.ad.alphablackbox.logic.cryptography.UCipher.Companion.decrypt
import java.io.File

class Player : Thread(){

    private var mp :MediaPlayer? = null
    private var threadWorking=false
    private var threadIsWorking=false
    var seekBar :SeekBar? = null


    override fun run() {
        threadIsWorking=true
        mp!!.start()
        while(threadWorking){
            sleep(1000)
            seekBar?.progress=((mp!!.currentPosition.toFloat()/mp!!.duration)*100).toInt()
        }
        threadIsWorking=false
    }

    fun play(file :String,context:Context) {
        // read data
        val (data, iv) = FileExplorer.readData(file)
        // decode data
        val decodedData = decrypt(data, iv)
        // create temporary file
        val temporaryFile = File(recordsDir, "test.wav")
        temporaryFile.createNewFile()
        temporaryFile.writeBytes(decodedData.toByteArray())
        // play
        val path = Uri.parse(temporaryFile.path)
        resetPlayer()
        mp=MediaPlayer()
        //setDataSource should be secured!
        mp!!.setDataSource(context,path)
        mp!!.prepare()
        threadWorking=true
        this.start()
        // delete temporary file ???
    }

    fun resetPlayer(){
        threadWorking=false
        while(threadIsWorking){
            sleep(500)
        }
        mp?.reset()
    }

    fun pause(){
        mp?.pause()
    }

    fun unpause(){
        mp?.start()
    }

    fun isPlaying():Boolean{
        var b = mp?.isPlaying
        if(b==null) {b=false}
        return b
    }

    fun setSpeed(speed:Float){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mp!!.playbackParams = mp!!.playbackParams.setSpeed(speed)
        }
        else{
            Log.d("App", "Invalid Build.VERSION: Build.VERSION.SDK_INT < Build.VERSION_CODES.M")
        }
    }

    fun setPosition(time:Int){
        mp?.seekTo(time)
    }

    fun getCurrentPosition():Int{
        if(mp!=null){
            return mp!!.getCurrentPosition()
        }
        else{
            return -1
        }
    }

    fun getDuration():Int{
        return mp!!.duration
    }
}

