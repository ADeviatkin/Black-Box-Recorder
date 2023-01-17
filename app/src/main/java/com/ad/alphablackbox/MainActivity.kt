package com.ad.alphablackbox

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ad.alphablackbox.logic.Permissions.Companion.ensurePermissions

import android.view.View
import com.ad.alphablackbox.logic.controll.OnClickManager
import com.ad.alphablackbox.logic.controll.SwipeListener

import com.ad.alphablackbox.logic.player.Player
import com.ad.alphablackbox.logic.load.FilesLoader
import android.os.Environment
import android.util.Log

import android.widget.SeekBar

class MainActivity : AppCompatActivity()
{
    private lateinit var onclickmanager : OnClickManager
    lateinit var swipelistener :SwipeListener
    val player=Player()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        ensurePermissions(this, applicationContext)
        initiate()
    }

    private fun initiate()
    {
        onclickmanager = OnClickManager(this)
        swipelistener = SwipeListener(findViewById(R.id.main_layout), onclickmanager.navigation())
        onclickmanager.setView(0)

        Log.d("App", "Media player created")


        val path = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        if(path == null){
            Log.d("App", "Can not access to Environment.DIRECTORY_DOWNLOADS")
        }
        else{
            Log.d("App", "Path $path")
            val fl = FilesLoader()
            val files = fl.getAllFiles(path)
            Log.d("App", "song 1")
            player.play(files[1],applicationContext)
            }
    }
    fun onClick(button :View)
    {
        onclickmanager.click(button)
    }
}
