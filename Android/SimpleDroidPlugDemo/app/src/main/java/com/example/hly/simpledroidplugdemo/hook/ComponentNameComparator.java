package com.example.hly.simpledroidplugdemo.hook;

import android.content.ComponentName;
import android.text.TextUtils;

import java.util.Comparator;

/**
 * Created by hly on 9/20/16.
 */
public class ComponentNameComparator implements Comparator<ComponentName> {

    @Override
    public int compare(ComponentName lhs, ComponentName rhs) {
        if (lhs == null && rhs == null) {
            return 0;
        } else if (lhs != null && rhs == null) {
            return 1;
        } else if (lhs == null && rhs != null) {
            return -1;
        } else {
            if (TextUtils.equals(lhs.getPackageName(), rhs.getPackageName()) && TextUtils.equals(lhs.getShortClassName(), rhs.getShortClassName())) {
                return 0;
            } else {
                return lhs.compareTo(rhs);
            }
        }
    }
}

