package com.hly.keyboard;

import android.view.View;
import android.view.Window;

/**
 * Created by hly on 2018/1/2.
 */

public interface KeyBoardWindowIMPL {

    void show(View anrch);

    void hide();

    boolean isShowing();

    void setWindow(Window window);
}
