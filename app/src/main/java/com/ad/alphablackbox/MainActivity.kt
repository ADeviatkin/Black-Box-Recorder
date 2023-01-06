package com.ad.alphablackbox

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.ad.alphablackbox.logic.Permissions.Companion.ensurePermissions
import com.ad.alphablackbox.logic.ServiceBridge.Companion.connectToService
import com.ad.alphablackbox.presentation.NavigationBar
import android.view.View
import com.ad.alphablackbox.logic.controll.SwipeListener

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
    }
    fun onClick(view :View)
    {
        navigation.switchView(view)
    }
}
