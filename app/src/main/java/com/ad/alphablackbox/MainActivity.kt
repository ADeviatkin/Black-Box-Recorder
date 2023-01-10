package com.ad.alphablackbox

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ad.alphablackbox.logic.Permissions.Companion.ensurePermissions

import android.view.View
import com.ad.alphablackbox.logic.controll.OnClickManager
import com.ad.alphablackbox.logic.controll.SwipeListener

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
    }
    fun onClick(button :View)
    {
        onclickmeneger.click(button)
    }
}
