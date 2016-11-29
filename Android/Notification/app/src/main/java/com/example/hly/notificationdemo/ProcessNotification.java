package com.example.hly.notificationdemo;

import android.content.Context;

/**
 * Created by hly on 11/28/16.
 */

public class ProcessNotification extends BaseNotification implements MainActivity.NotificationCompatImpl {
    public ProcessNotification(Context context) {
        super(context);
    }

    @Override
    public void showNotification() {
        builder.setTicker("ProcessNotification");
        builder.setContentTitle("ProcessNotification");
        builder.setContentText("ProcessNotificationProcessNotificationProcessNotification");

        // Start a lengthy operation in a background thread
        new Thread(
            new Runnable() {
                @Override
                public void run() {
                    int incr;
                    // Do the "lengthy" operation 20 times
                    for (incr = 0; incr <= 100; incr+=5) {
                        // Sets the progress indicator to a max value, the
                        // current completion percentage, and "determinate"
                        // state
                        builder.setProgress(100, incr, false);
                        // Displays the progress bar for the first time.
                        manager.notify(0, builder.build());
                        // Sleeps the thread, simulating an operation
                        // that takes time
                        try {
                            // Sleep for 1 seconds
                            Thread.sleep(1*1000);
                        } catch (InterruptedException e) {

                        }
                    }
                    // When the loop is finished, updates the notification
                    builder.setContentText("Download complete");
                    // Removes the progress bar
                    builder.setProgress(0,0,false);
                    manager.notify(0, builder.build());
                }
            }
        // Starts the thread by calling the run() method in its Runnable
        ).start();
    }
}
