package com.example.hly.graphicdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener{
    private TextView animTv;
    private Button dumpBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        animTv = (TextView) findViewById(R.id.anima);
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 500, 0, 0);
        translateAnimation.setRepeatCount(Animation.INFINITE);
        translateAnimation.setRepeatMode(Animation.REVERSE);
        translateAnimation.setDuration(3000);
        animTv.setAnimation(translateAnimation);
        translateAnimation.start();

        dumpBt = (Button) findViewById(R.id.dump);
        dumpBt.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dump:
                Dump d = new Dump();
                d.doDump(MainActivity.this);
                break;
        }
    }
}
