package com.ad.blackboxrecorder.activities.Main.gui;

import android.view.View;
import android.widget.ImageButton;

import com.ad.blackboxrecorder.activities.Main.MainActivity;
import com.ad.blackboxrecorder.R;

public class ButtonManager {
    private static boolean isWaiting = false;  // default value
    private static boolean isButtonClicked = false;  // default value
    private static View view;
    private static ImageButton myButton;

    private static boolean startStatus = false;
    public ButtonManager(View view){
        ButtonManager.view = view;
    }
    public static void handleRecordingButton() {
        myButton = view.findViewById(R.id.recordButton);
        if(startStatus){
            myButton.setSelected(true);
        }
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isWaiting){
                    isWaiting = true;
                    if (!myButton.isSelected()){
                        MainActivity.ServiceBridge.connect();
                        myButton.setSelected(true);
                    }
                    else{
                        MainActivity.ServiceBridge.disconnect();
                        myButton.setSelected(false);
                    }
                    isWaiting = false;
                }
            }
        });
    }
    public static void changeRecordButtonStatus(boolean clicked){
        if (clicked){
            startStatus = true;
            if (myButton != null){
                myButton.setSelected(true);
            }
        }

    }
}
