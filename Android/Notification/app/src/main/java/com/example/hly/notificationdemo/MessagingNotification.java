package com.example.hly.notificationdemo;

import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

/**
 * Created by hly on 11/24/16.
 */

public class MessagingNotification extends BaseNotification implements MainActivity.NotificationCompatImpl {


    public MessagingNotification(Context context) {
        super(context);
    }

    public void showTwo() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Toast.makeText(mContext, "ur device is not support media style, use stand instead", Toast.LENGTH_SHORT).show();
        }
        builder.setTicker("MessagingNotification");
        builder.setContentTitle("MessagingNotification");
        builder.setContentText("MessagingNotificationMessagingNotificationMessagingNotification");

        builder.setStyle(new NotificationCompat.MessagingStyle("DisplayName2")
                .addMessage("addMessageA", (int)System.currentTimeMillis(), "sender1")
                .addMessage("addMessageB", (int)System.currentTimeMillis(), "sender2")
                .addMessage("addMessageC", (int)System.currentTimeMillis(), "sender3")
                .addMessage("addMessageD", (int)System.currentTimeMillis(), "sender4")
                .setConversationTitle("ConversationTitle"));

        manager.notify((int)System.currentTimeMillis(), getNotification());
    }

    @Override
    public void showNotification() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Toast.makeText(mContext, "ur device is not support message style, use stand instead", Toast.LENGTH_SHORT).show();
        }
        builder.setTicker("MessagingNotification");
        builder.setContentTitle("MessagingNotification");
        builder.setContentText("MessagingNotificationMessagingNotificationMessagingNotification");

        builder.setStyle(new NotificationCompat.MessagingStyle("DisplayName")
                .addMessage("addMessage1", (int)System.currentTimeMillis(), "sender1")
                .addMessage("addMessage2", (int)System.currentTimeMillis(), "sender2")
                .addMessage("addMessage3", (int)System.currentTimeMillis(), "sender3")
                .addMessage("addMessage4", (int)System.currentTimeMillis(), "sender4")
                .setConversationTitle("ConversationTitle"));

        manager.notify((int)System.currentTimeMillis(), getNotification());
    }
}
