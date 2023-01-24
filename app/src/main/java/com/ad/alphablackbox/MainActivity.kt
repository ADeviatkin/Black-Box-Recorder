package com.ad.alphablackbox

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.ad.alphablackbox.logic.Permissions.Companion.ensurePermissions
import com.ad.alphablackbox.logic.ServiceBridge
import android.view.View
import android.widget.EditText
import androidx.annotation.RequiresApi
import com.ad.alphablackbox.logic.controll.OnClickManager
import com.ad.alphablackbox.logic.controll.SwipeListener
import com.ad.alphablackbox.logic.cryptography.UCipher
import com.ad.alphablackbox.logic.player.Player
import java.nio.file.Path


class MainActivity : AppCompatActivity()
{
    companion object {
        lateinit var recordsDir: String
    }
    private lateinit var onclickmanager : OnClickManager
    lateinit var bridgeToRecorderService: ServiceBridge
    lateinit var swipelistener :SwipeListener
    var cipherAES: UCipher? = null
    val player = Player()
    lateinit var timeTextBox :EditText

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        // ensure permissions
        ensurePermissions(this, applicationContext)

        // create Cipher with AES encrypting
        this.cipherAES = UCipher()

        // define path for recording
        recordsDir = externalCacheDir?.path.toString()

        // open interface for control service
        bridgeToRecorderService = ServiceBridge(applicationContext)

        // initiate GUI manager
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
    fun initializedObject() : Boolean
    {
        return this::timeTextBox.isInitialized
    }
}
