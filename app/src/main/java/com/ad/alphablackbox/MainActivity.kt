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

class MainActivity : AppCompatActivity()
{
    private lateinit var onclickmeneger : OnClickManager
    lateinit var swipelistener :SwipeListener

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        ensurePermissions(this, applicationContext)
        initiate()
    }

    private fun initiate()
    {
        onclickmeneger = OnClickManager(this)
        swipelistener = SwipeListener(findViewById(R.id.main_layout), onclickmeneger.navigation())
        onclickmeneger.setView(0)

        val p=Player()
        Log.d("App", "Media player created")

        val path = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        if(path == null){
            Log.d("App", "Can not access to Environment.DIRECTORY_DOWNLOADS")
        }
        else{
            Log.d("App", "Path $path")
            val fl = FilesLoader()
            val files = fl.getAllFiles(path)
            //p.play(files[1],applicationContext)
            //p.setPosition(20000)
            //Log.d("App - player", p.getCurrentPosition().toString())
            //p.setSpeed(2.0f)
            //Thread.sleep(10000)
            //p.pause()
            //Thread.sleep(20000)
            //p.unpause()
            }
    }
    fun onClick(button :View)
    {
        onclickmeneger.click(button)
    }
}
