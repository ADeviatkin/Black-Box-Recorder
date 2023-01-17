package com.ad.alphablackbox

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ad.alphablackbox.logic.Permissions.Companion.ensurePermissions
import com.ad.alphablackbox.logic.ServiceBridge
import android.view.View
import com.ad.alphablackbox.logic.controll.OnClickManager
import com.ad.alphablackbox.logic.controll.SwipeListener
import com.ad.alphablackbox.logic.player.Player


class MainActivity : AppCompatActivity()
{
    private lateinit var onclickmanager : OnClickManager
    lateinit var bridgeToRecorderService: ServiceBridge
    lateinit var swipelistener :SwipeListener
    val player=Player()
    val records_dir = externalCacheDir?.path

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        ensurePermissions(this, applicationContext)
        bridgeToRecorderService = ServiceBridge(applicationContext)
        initiate()
    }

    override fun onDestroy()
    {
        super.onDestroy()
    }

    private fun initiate()
    {
        onclickmanager = OnClickManager(this)
        swipelistener = SwipeListener(findViewById(R.id.main_layout), onclickmanager.navigation())
        onclickmanager.setView(0)
    }
    fun onClick(button :View)
    {
        onclickmanager.click(button)
    }
}
