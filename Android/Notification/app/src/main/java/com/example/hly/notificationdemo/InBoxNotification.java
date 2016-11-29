package com.example.hly.notificationdemo;

import android.content.Context;
import android.support.v4.app.NotificationCompat;

/**
 * Created by hly on 11/24/16.
 */

public class InBoxNotification extends BaseNotification implements MainActivity.NotificationCompatImpl {
    public InBoxNotification(Context context) {
        super(context);
    }

    @Override
    public void showNotification() {
        builder.setTicker("InBoxNotification");
        builder.setContentTitle("InBoxNotification");
        builder.setContentText("InBoxNotificationInBoxNotification");
        builder.setStyle(new NotificationCompat.InboxStyle()
                .setBigContentTitle("BigContentTitle")
                .setSummaryText("summartText")
                .addLine("1. first")
                .addLine("2. second")
                .addLine("3. third")
                .addLine("4. fouth")
                .addLine("5. five")
                .addLine("6. six")
                .addLine("7. seven")
                .addLine("8. eight")
                .addLine("9. nine")
                .addLine("10. ten")
                .addLine("11. eleven"));

        manager.notify((int) System.currentTimeMillis(), getNotification());
    }
}
