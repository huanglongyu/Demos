package com.example.hly.javademo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import static android.content.Intent.ACTION_VIEW;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.remove).setOnClickListener(this);
        findViewById(R.id.add).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.remove:
                Intent remove = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
                remove.putExtra(Intent.EXTRA_SHORTCUT_NAME, "123");

                //have to add ACTION_VIEW action if u do not set action
                Intent action2 = new Intent(MainActivity.this, MainActivity.class);
                action2.setAction(ACTION_VIEW);

                remove.putExtra(Intent.EXTRA_SHORTCUT_INTENT, action2);

                sendBroadcast(remove);
                break;

            case R.id.add:
                Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
                shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, "123");
                Intent.ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(this, R.mipmap.ic_launcher);
                shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);

                Intent action = new Intent(MainActivity.this, MainActivity.class);

                shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, action);
                sendBroadcast(shortcut);
                break;
        }
    }
}
