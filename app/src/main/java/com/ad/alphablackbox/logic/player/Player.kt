package com.ad.alphablackbox.logic.player

import android.app.Activity
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import java.io.File

class Player : Activity() {

    private var mp :MediaPlayer? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        volumeControlStream = AudioManager.STREAM_MUSIC
    }

    fun Play(file :File) {
        val path = Uri.parse(file.path)
        if (mp != null) {
            mp!!.release()
        }
        mp = MediaPlayer.create(this,path)
        mp?.start()
    }

    fun Pause(){
        mp?.pause()
    }

    fun Resume(){
        mp?.start()
    }

    fun Isplaying():Boolean{
        val b = mp?.isPlaying
        if(b!=null) {return b}
        else {return false}
    }
}