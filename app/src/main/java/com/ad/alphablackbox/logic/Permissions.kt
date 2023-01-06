package com.ad.alphablackbox.logic

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ad.alphablackbox.MainActivity

class Permissions {
    companion object{
        fun ensurePermissions(mainActivity: MainActivity, applicationContext: Context) {
            if (!Permissions.allAllowed(applicationContext)){
                Permissions.requestAll(mainActivity)
            }
        }
        private fun allAllowed(applicationContext: Context) : Boolean {
            return ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        }

        private fun requestAll(activity: AppCompatActivity) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                123
            )
        }
    }
}