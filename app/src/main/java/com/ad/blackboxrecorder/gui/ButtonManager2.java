package com.ad.blackboxrecorder.gui;

import android.view.View;
import android.widget.ImageButton;

import com.ad.blackboxrecorder.R;

import java.util.concurrent.TimeUnit;

public class ButtonManager2 {
    private static boolean isWaiting = false;  // default value
    private static boolean isButtonClicked = false;  // default value
    public static void handleButtons(View view) {
        ImageButton myButton = view.findViewById(R.id.recordButton);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isWaiting){
                    isWaiting = true;
                    if (!myButton.isSelected()){
                        myButton.setSelected(true);
                    }
                    else{
                        myButton.setSelected(false);
                    }
                    isWaiting = false;
                }
            }
        });
    }
}
