package com.ad.alphablackbox.presentation

import android.annotation.SuppressLint
import android.graphics.Color
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.ListView
import com.ad.alphablackbox.MainActivity
import com.ad.alphablackbox.R
import com.ad.alphablackbox.logic.load.FilesLoader

class NavigationManager(del : MainActivity)
{
    private var currentView :Int = 0;
    private val delegate :MainActivity = del

    private val layoutIds :IntArray = intArrayOf(R.layout.main_layout, R.layout.records_layout, R.layout.settings_layout)
    private val viewIds :IntArray = intArrayOf(R.id.main_layout, R.id.records_layout, R.id.settings_layout)
    private val navBarIds :IntArray = intArrayOf(R.id.navigation_bar_indicator_1, R.id.navigation_bar_indicator_2, R.id.navigation_bar_indicator_3)

    // private
    private fun showFileList()
    {
        val loader = FilesLoader()
        val filelist = loader.getFileNames("/data/data/com.ad.alphablackbox/files/Download")
        //file list view
        var flv :ListView = delegate.findViewById(R.id.filelist)

        // Adapter parms
        var adapter = ArrayAdapter<String>(delegate, R.layout.list_view_module, R.id.custom_id2 , filelist)
        flv.adapter = adapter
    }
    // public
    fun setView(c :Int)
    {
        delegate.setContentView(layoutIds[c])
        // listener
        delegate.findViewById<View>(viewIds[c]).setOnTouchListener(delegate.swipelistener)
        // setting new color
        delegate.findViewById<View>(navBarIds[c]).setBackgroundColor(Color.WHITE)

        if (viewIds[c] == R.id.records_layout)
        {
            showFileList()
        }
        currentView = c
    }
    // switches
    fun switchRight()
    {
        var c :Int = currentView
        if (c < 2) c += 1

        setView(c)
    }
    fun switchLeft()
    {
        var c :Int = currentView
        if (c > 0) c -= 1

        setView(c)
    }
}