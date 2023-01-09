package com.ad.alphablackbox.presentation

import android.graphics.Color
import android.util.Log
import android.view.View
import com.ad.alphablackbox.MainActivity
import com.ad.alphablackbox.R

class NavigationBar(del : MainActivity)
{
    private var currentView :Int = 0;
    private val layoutIds :IntArray = intArrayOf(R.layout.main_layout, R.layout.records_layout, R.layout.settings_layout)
    private val viewIds :IntArray = intArrayOf(R.id.main_layout, R.id.records_layout, R.id.settings_layout)
    private val navBarIds :IntArray = intArrayOf(R.id.navigation_bar_indicator_1, R.id.navigation_bar_indicator_2, R.id.navigation_bar_indicator_3)
    private val delegate :MainActivity = del

    fun setView(c :Int)
    {
        delegate.setContentView(layoutIds[c])
        // listener
        var v :View = delegate.findViewById(viewIds[c])
        v.setOnTouchListener(delegate.swipelistener)
        // setting new color
        v = delegate.findViewById(navBarIds[c])
        v.setBackgroundColor(Color.WHITE)

        currentView = c
    }
    // public
    fun switchView(sender :View)
    {
        when (sender.id)
        {
            R.id.button_main_view -> setView(0)
            R.id.button_records_view -> setView(1)
            R.id.button_settings_view -> setView(2)
            else -> Log.d("App", "Error occurred in Toolbar - Layout index out of range, layout index = "+sender.id.toString())
        }
    }
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