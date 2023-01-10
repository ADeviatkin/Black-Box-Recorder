package com.ad.alphablackbox

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.ad.alphablackbox.logic.Permissions.Companion.ensurePermissions
import com.ad.alphablackbox.logic.ServiceBridge.Companion.connectToService
import com.ad.alphablackbox.presentation.NavigationBar
import android.view.View
import com.ad.alphablackbox.logic.controll.SwipeListener

import com.ad.alphablackbox.logic.player.Player
import com.ad.alphablackbox.logic.load.FilesLoader
import android.os.Environment


class MainActivity : AppCompatActivity()
{
    private lateinit var navigation :NavigationBar
    lateinit var swipelistener :SwipeListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ensurePermissions(this, applicationContext)
        initiate()
        Log.d("App", "Try to connect")
        connectToService(applicationContext)
    }
    private fun initiate()
    {
        navigation = NavigationBar(this)
        swipelistener = SwipeListener(findViewById(R.id.main_layout), navigation)
        navigation.setView(0)

        val p=Player()
        Log.d("App", "Media player created")

        val path = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        if(path == null){
            Log.d("App", "Can not access to Environment.DIRECTORY_DOWNLOADS")
        }
        else{
            Log.d("App", "Path $path")
            val fl = FilesLoader()
            val files = fl.getAllFiles(path.toString())
            var i=0
            for (file in files) {
                Log.d("App","$i : $file")
                i++
            }
            p.play(files[1],applicationContext)
            Log.d("App", "Song playing started")
        }
    }
    fun onClick(view :View)
    {
        navigation.switchView(view)
    }
}
