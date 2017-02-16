package com.example.hly.simpledroidplugdemo.hook;

import android.content.Context;
import android.util.Log;

import java.io.File;

/**
 * Created by hly on 9/20/16.
 */
public class DirHelper {
    public static final String EXTRA_TARGET_INTENT = "EXTRA_TARGET_INTENT";

    private static File sBaseDir = null;

    private static void init(Context context) {
        if (sBaseDir == null) {
            sBaseDir = new File(context.getCacheDir().getParentFile(), "Plugin");
            enforceDirExists(sBaseDir);
        }
    }

    //data/user/0/com.example.hly.simpledroidplugdemo/Plugin/com.ludashi.vrbench/apk/base-1.apk
    public static String getPluginApkFile(Context context, String pluginInfoPackageName) {
        return new File(getPluginApkDir(context, pluginInfoPackageName), "base-1.apk").getPath();
    }

    public static String getPluginApkDir(Context context, String pluginInfoPackageName) {
        return enforceDirExists(new File(makePluginBaseDir(context, pluginInfoPackageName), "apk"));
    }


    public static void cleanOptimizedDirectory(String optimizedDirectory) {
        try {
            File dir = new File(optimizedDirectory);
            if (dir.exists() && dir.isDirectory()) {
                File[] files = dir.listFiles();
                if (files != null && files.length > 0) {
                    for (File f : files) {
                        f.delete();
                    }
                }
            }

            if (dir.exists() && dir.isFile()) {
                dir.delete();
                dir.mkdirs();
            }
        } catch (Throwable e) {
        }
    }

    public static String makePluginBaseDir(Context context, String pluginInfoPackageName) {
        init(context);
        return enforceDirExists(new File(sBaseDir, pluginInfoPackageName));
    }

    private static String enforceDirExists(File file) {
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getPath();
    }

    public static String getPluginNativeLibraryDir(Context context, String pluginInfoPackageName) {
        return enforceDirExists(new File(makePluginBaseDir(context, pluginInfoPackageName), "lib"));
    }

    public static String getPluginDalvikCacheDir(Context context, String pluginInfoPackageName) {
        return enforceDirExists(new File(makePluginBaseDir(context, pluginInfoPackageName), "dalvik-cache"));
    }

    public static String getPluginDataDir(Context context, String pluginInfoPackageName) {
        return enforceDirExists(new File(makePluginBaseDir(context, pluginInfoPackageName), "data/" + pluginInfoPackageName));
    }
}
