package com.example.hly.simpledroidplugdemo.hook;

import android.os.UserHandle;

import com.example.hly.simpledroidplugdemo.hook.utils.MethodUtils;

/**
 * Created by hly on 9/20/16.
 */
public class UserHandleCompat {
    //    UserHandle.getCallingUserId()
    public static int getCallingUserId() {
        try {
            return (int) MethodUtils.invokeStaticMethod(UserHandle.class, "getCallingUserId");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
