package com.ad.alphablackbox.presentation

import android.util.Log
import android.view.View
import com.ad.alphablackbox.R
import com.google.android.material.button.MaterialButton

class Toolbar {
    public fun switchView(view : View)
    {
        var sender :MaterialButton = view as MaterialButton

        when (sender.id)
        {
            R.id.button_main_view -> Log.d("Toolbar", "Main view button clicked")
            R.id.button_records_view -> Log.d("Toolbar", "Records view button clicked")
            R.id.button_settings_view -> Log.d("Toolbar", "Settings view button clicked")
            else -> Log.d("Toolbar", "Error occurred")
        }
    }
}