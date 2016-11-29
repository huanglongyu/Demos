package com.example.hly.notificationdemo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by hly on 11/27/16.
 */

public class ActivityA extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView view = new TextView(this);
        view.setText("ActivityA");
        setContentView(view);
    }
}
