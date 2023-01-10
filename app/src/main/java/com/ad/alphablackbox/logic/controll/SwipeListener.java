package com.ad.alphablackbox.logic.controll;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.ad.alphablackbox.presentation.NavigationManager;

public class SwipeListener implements View.OnTouchListener
{
    GestureDetector detector;
    int move_treshold = 100;
    int velocity_treshold = 100;

    // Constructor
    public SwipeListener(View view, NavigationManager navigation)
    {
        GestureDetector.SimpleOnGestureListener listener = new GestureDetector.SimpleOnGestureListener()
        {
            @Override
            public boolean onDown(MotionEvent e)
            {
                // screen touched
                return true;
            }

            @Override
            public boolean onFling(MotionEvent event1, MotionEvent event2, float velX, float velY)
            {
                // swipe
                float xMove = event2.getX() - event1.getX();
                float yMove = event2.getY() - event1.getY();
                if (Math.abs(xMove) > Math.abs(yMove) && Math.abs(xMove) > move_treshold && Math.abs(velX) > velocity_treshold)
                {
                    // If it was horizontal swipe indeed
                    if (xMove > 0) // right
                    {
                        navigation.switchLeft();
                    }
                    else // left
                    {
                        navigation.switchRight();
                    }
                    return true;
                }
                return false;
            }
        };

        detector = new GestureDetector(listener);
    }

    // Methods
    @Override
    public boolean onTouch(View view, MotionEvent me)
    {
        return detector.onTouchEvent(me);
    }
}
