package com.ad.alphablackbox.logic.controll

import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import com.ad.alphablackbox.MainActivity
import com.ad.alphablackbox.MainActivity.Companion.recordsDir
import com.ad.alphablackbox.R
import com.ad.alphablackbox.logic.ServiceBridge
import com.ad.alphablackbox.presentation.NavigationManager
import java.io.File

class OnClickManager (del : MainActivity)
{
    private var navigation : NavigationManager = NavigationManager(del)
    private var activity :MainActivity = del

    // public
    fun navigation(): NavigationManager
    {
        return navigation
    }
    fun setView(x :Int)
    {
        navigation.setView(x)
    }
    fun click(sender :View)
    {
        when (sender.id)
        {
            // switch view
            R.id.button_main_view -> navigation.setView(0)
            R.id.button_records_view -> navigation.setView(1)
            R.id.button_settings_view -> navigation.setView(2)
            // buttons main view
            R.id.button_start_recording -> startRecording()
            R.id.button_stop_recording -> stopRecording()
            R.id.button_pause -> pause()
            R.id.item -> play(sender as Button)
            else -> Log.d("App", "Error occurred in OnClickManager -> Unknown button was clicked (View index out of range, View index = "+sender.id.toString()+")")
        }
    }

    // private
    private fun play(sender :Button)
    {
        var path = recordsDir + "/" + sender.text
        activity.player.play(path, activity.applicationContext)
    }
    private fun<T> getView(id :Int) :T? where T:View
    {
        try
        {
            return activity.findViewById(id) as T
        }
        catch (ex :Exception)
        {
            Log.d("App", "Unable to reach the resource with id "+id.toString()+" wih exception message: "+ex.message)
        }
        return null
    }

    private fun pause()
    {
        val view = getView<Button>(R.id.button_pause)
        if(activity.player.isPlaying())
        {
            view?.setText("Resume")
            activity.player.pause()
        }
        else
        {
            view?.setText("Pause")
            activity.player.unpause()
        }
    }

    private fun startRecording()
    {
        // Function begins recording (starts the background service)

        Log.d("App", "Try to connect")
        activity.bridgeToRecorderService.connect()

        getView<ImageButton>(R.id.button_start_recording)?.isEnabled = false
        getView<Button>(R.id.button_stop_recording)?.isEnabled = true
    }
    private fun stopRecording()
    {
        // Function stops recording (stops the background service)
        activity.bridgeToRecorderService.disconnect()

        getView<ImageButton>(R.id.button_start_recording)?.isEnabled = true
        getView<Button>(R.id.button_stop_recording)?.isEnabled = false
    }
}