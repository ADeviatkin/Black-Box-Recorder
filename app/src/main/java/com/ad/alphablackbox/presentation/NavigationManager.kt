package com.ad.alphablackbox.presentation

import android.graphics.Color
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import com.ad.alphablackbox.MainActivity
import com.ad.alphablackbox.MainActivity.Companion.recordsDir
import com.ad.alphablackbox.R
import com.ad.alphablackbox.logic.FileExplorer
import com.ad.alphablackbox.logic.recorder.RecordingVariables

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
        var file_list :MutableList<String> = FileExplorer.getFileNames(recordsDir.toString()).toMutableList()
        file_list.remove("cache")
        val adapter = ArrayAdapter<String>(activity, R.layout.list_view_module, R.id.item , file_list)
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
    private fun setTextListener()
    {
        if (!activity.initializedObject())
        {
            activity.timeTextBox = activity.findViewById<EditText>(R.id.editTextTime)
            activity.timeTextBox.addTextChangedListener(object : TextWatcher {

                override fun afterTextChanged(s : Editable?) {}

                override fun beforeTextChanged(s :CharSequence?, start :Int, count :Int, after :Int) {}

                override fun onTextChanged(s :CharSequence?, start :Int, before :Int, count :Int)
                {
                    try
                    {
                        RecordingVariables.timePeriod = s.toString().toLong()
                    }
                    catch (ex :java.lang.Exception)
                    {
                        RecordingVariables.timePeriod = RecordingVariables.defaultTimePeriod
                    }
                }
            })
        }
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
        else if (viewIds[c] == R.id.settings_layout)
        {
            setTextListener()
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