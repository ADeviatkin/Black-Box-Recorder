package com.ad.alphablackbox

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.ad.alphablackbox.logic.Permissions.Companion.ensurePermissions
import com.ad.alphablackbox.logic.ServiceBridge.Companion.connectToService
import com.ad.alphablackbox.presentation.Toolbar
import android.view.View

class MainActivity : AppCompatActivity()
{
    private var toolbar = Toolbar()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)
        ensurePermissions(this, applicationContext)
        Log.d("App", "Try to connect")
        connectToService(applicationContext)
    }
    fun onClick(view :View)
    {
        toolbar.switchView(view)
    }
}

