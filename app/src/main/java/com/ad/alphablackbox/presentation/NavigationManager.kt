package com.ad.alphablackbox.presentation

import android.graphics.Color
import android.util.Log
import android.view.View
import com.ad.alphablackbox.MainActivity
import com.ad.alphablackbox.R

class NavigationManager(del : MainActivity)
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
        delegate.findViewById<View>(viewIds[c]).setOnTouchListener(delegate.swipelistener)
        // setting new color
        delegate.findViewById<View>(navBarIds[c]).setBackgroundColor(Color.WHITE)

        currentView = c
    }
    // public
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