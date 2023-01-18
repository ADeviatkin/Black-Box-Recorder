package com.ad.alphablackbox.logic.player

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.util.Log
import com.ad.alphablackbox.MainActivity.Companion.recordsDir
import com.ad.alphablackbox.logic.FileExplorer
import com.ad.alphablackbox.logic.cryptography.UCipher.Companion.decrypt
import java.io.File

class Player : Thread(){

    private var mp :MediaPlayer? = null

    override fun run() {
        //mp!!.start()
        sleep(mp!!.duration.toLong())
    }

    fun play(file :String,context:Context) {
        // read data
        var (data, iv) = FileExplorer.readData(file)
        // decode data
        var decodedData = decrypt(data, iv)
        // create temporary file
        var file = File(recordsDir, "test.wav")
        file.createNewFile();
        file.writeBytes(decodedData.toByteArray())
        // play
        val path = Uri.parse(file.path)
        if (mp != null) {
            mp!!.release()
        }
        mp = MediaPlayer()
        mp!!.setDataSource(context,path)
        mp!!.prepare()
        this.start()
        // delete temporary file ???
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
            mp!!.setPlaybackParams(mp!!.getPlaybackParams().setSpeed(speed))
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

