package com.ad.alphablackbox.presentation

import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.SeekBar
import com.ad.alphablackbox.MainActivity
import com.ad.alphablackbox.MainActivity.Companion.recordsDir
import com.ad.alphablackbox.R
import com.ad.alphablackbox.logic.FileExplorer

class NavigationManager(del : MainActivity)
{
    private var currentView :Int = 0;
    private val activity :MainActivity = del
    private var seek:SeekBar?=null

    private val layoutIds :IntArray = intArrayOf(R.layout.main_layout, R.layout.records_layout, R.layout.settings_layout)
    private val viewIds :IntArray = intArrayOf(R.id.main_layout, R.id.records_layout, R.id.settings_layout)
    private val navBarIds :IntArray = intArrayOf(R.id.navigation_bar_indicator_1, R.id.navigation_bar_indicator_2, R.id.navigation_bar_indicator_3)

    // private
    private fun showFileList(){
        //file list view
        val flv :ListView = activity.findViewById(R.id.filelist)

        // Adapter parms
        val adapter = ArrayAdapter<String>(activity, R.layout.list_view_module, R.id.item , FileExplorer.getFileNames(recordsDir.toString()))
        flv.adapter = adapter
    }
    private fun setSeekBar()
    {
        seek = activity.findViewById<SeekBar>(R.id.seekBar)
        seek?.setOnSeekBarChangeListener(object :
                SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar, progress: Int, fromUser: Boolean){
                if(fromUser)
                {
                    activity.player.setPosition((progress*activity.player.getDuration()/100))
                    activity.player.unpause()
                    Log.d("App", "set progress")
                }
            }

            override fun onStartTrackingTouch(seek: SeekBar){
                // write custom code for progress is started
            }

            override fun onStopTrackingTouch(seek: SeekBar){
                // write custom code for progress is stopped
            }
        })
        activity.player.seekBar=seek
    }
    // public
    fun setView(c :Int)
    {
        activity.setContentView(layoutIds[c])
        // listener
        activity.findViewById<View>(viewIds[c]).setOnTouchListener(activity.swipelistener)
        // setting new color
        activity.findViewById<View>(navBarIds[c]).setBackgroundColor(Color.WHITE)

        if (viewIds[c] == R.id.records_layout)
        {
            if(seek == null) setSeekBar()
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