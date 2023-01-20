package com.ad.alphablackbox.logic.player

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.SeekBar
import com.ad.alphablackbox.MainActivity.Companion.recordsDir
import com.ad.alphablackbox.logic.FileExplorer
import com.ad.alphablackbox.logic.cryptography.UCipher.Companion.decrypt
import java.io.File


class Player : Runnable{

    private var mediaPlayer :MediaPlayer? = null
    private var threadWorking=false
    private var threadIsWorking=false
    var seekBar :SeekBar? = null
    var thread :Thread? = null

    override fun run() {
        threadIsWorking=true
        mediaPlayer!!.start()
        while(threadWorking){
            Thread.sleep(1000)
            seekBar?.progress=((mediaPlayer!!.currentPosition.toFloat()/mediaPlayer!!.duration)*100).toInt()
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
        mediaPlayer=MediaPlayer()
        //setDataSource should be secured!
        mediaPlayer!!.setDataSource(context,path)
        mediaPlayer!!.prepare()
        threadWorking=true
        thread = Thread(this)
        thread!!.start()
        // delete temporary file ???
    }

    fun resetPlayer(){
        threadWorking=false
        while(threadIsWorking){
            Thread.sleep(500)
        }
        mediaPlayer?.reset()
    }

    fun pause(){
        mediaPlayer?.pause()
    }

    fun unpause(){
        mediaPlayer?.start()
    }

    fun isPlaying():Boolean{
        var b = mediaPlayer?.isPlaying
        if(b==null) {b=false}
        return b
    }

    fun setSpeed(speed:Float){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mediaPlayer!!.playbackParams = mediaPlayer!!.playbackParams.setSpeed(speed)
        }
        else{
            Log.d("App", "Invalid Build.VERSION: Build.VERSION.SDK_INT < Build.VERSION_CODES.M")
        }
    }

    fun setPosition(time:Int){
        mediaPlayer?.seekTo(time)
    }

    fun getCurrentPosition():Int{
        if(mediaPlayer!=null){
            return mediaPlayer!!.getCurrentPosition()
        }
        else{
            return -1
        }
    }

    fun getDuration():Int{
        return mediaPlayer!!.duration
    }
}

