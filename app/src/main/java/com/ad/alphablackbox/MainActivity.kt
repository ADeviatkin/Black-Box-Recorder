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
    private lateinit var onclickmanager : OnClickManager
    lateinit var swipelistener :SwipeListener
    var player = Player()

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

        Log.d("App", externalCacheDir?.path.toString())
        var loader = FilesLoader()
        var list1 = loader.getAllFiles(externalCacheDir?.path.toString())
        Log.d("App", list1.toString())
    }
    fun onClick(button :View)
    {
        onclickmanager.click(button)
    }
}
