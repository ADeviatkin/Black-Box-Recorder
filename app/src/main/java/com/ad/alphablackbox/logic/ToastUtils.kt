package com.ad.alphablackbox.logic

import android.content.Context
import android.widget.Toast

class ToastUtils {
    companion object{
        fun makeStartText(context: Context){
            Toast.makeText(context, "Service started by user.", Toast.LENGTH_LONG).show()
        }
        fun makeDestroyText(context: Context){
            Toast.makeText(context, "Service destroyed by user.", Toast.LENGTH_LONG).show()
        }
    }
}