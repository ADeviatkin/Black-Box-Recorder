package com.ad.alphablackbox.logic.player

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import java.io.File

class Player {

    private var mp :MediaPlayer? = null

    fun play(file :File,context:Context) {
        val path = Uri.parse(file.path)
        if (mp != null) {
            mp!!.release()
        }
        mp = MediaPlayer()
        mp?.setDataSource(context,path)
        mp?.prepare()
        mp?.start()
    }

    fun pause(){
        mp?.pause()
    }

    fun resume(){
        mp?.start()
    }

    fun isPlaying():Boolean{
        val b = mp?.isPlaying
        if(b!=null) {return b}
        else {return false}
    }
}