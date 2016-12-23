package com.example.hly.notificationdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by hly on 11/27/16.
 */

public class ActivityA extends Activity implements View.OnClickListener{
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        button = new Button(this);
        button.setText("ActivityA");
        button.setOnClickListener(this);
        setContentView(button);
    }

    @Override
    public void onClick(View view) {
        button.setText("Onclicked");
    }
}
