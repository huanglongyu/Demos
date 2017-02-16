package com.example.hly.simpledroidplugdemo;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.hly.simpledroidplugdemo.hook.AMS.AMSHookHelper;
import com.example.hly.simpledroidplugdemo.hook.ClassLoaderHookHelper;
import com.example.hly.simpledroidplugdemo.hook.DirHelper;
import com.example.hly.simpledroidplugdemo.hook.NativeLibraryHelperCompat;
import com.example.hly.simpledroidplugdemo.hook.PM.PluginPackageParser;
import com.example.hly.simpledroidplugdemo.hook.PlugManager;
import com.example.hly.simpledroidplugdemo.hook.Utils;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import dalvik.system.DexClassLoader;


public class MainActivity extends Activity implements View.OnClickListener{
    private static final String PLUG_IN_PATH = "/sdcard/app-ludashi-debug-unaligned.apk";
    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button bt = (Button) findViewById(R.id.test);
        bt.setOnClickListener(this);

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        try {
            //add new dexclassloader into application
            ClassLoaderHookHelper.hookLoadedApkInActivityThread(newBase, new File(PLUG_IN_PATH));

            //hook ams
            AMSHookHelper.hookAMS(newBase);
            PlugManager.init(newBase);
        } catch (ClassNotFoundException e) {
            Log.i(TAG, "MainActivity", e);
        } catch (NoSuchMethodException e) {
            Log.i(TAG, "MainActivity", e);
        } catch (InvocationTargetException e) {
            Log.i(TAG, "MainActivity", e);
        } catch (IllegalAccessException e) {
            Log.i(TAG, "MainActivity", e);
        } catch (NoSuchFieldException e) {
            Log.i(TAG, "MainActivity", e);
        } catch (InstantiationException e) {
            Log.i(TAG, "MainActivity", e);
        }
    }

    @Override
    public void onClick(View view) {
        try {
            installPackage(PLUG_IN_PATH, 0);

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(new ComponentName("com.ludashi.vrbench","com.ludashi.vrbench.pages.EntryActivity"));
//            intent.setClassName("com.ludashi.vrbench", "com.ludashi.vrbench.pages.EntryActivity");
            startActivity(intent);

//            Intent in = new Intent(this, StubActivity.class);
//            startActivity(in);
        }catch (RemoteException e) {
            Log.i(TAG, "MainActivity", e);
        }
    }

    private PackageInfo installPackage(String filepath, int flags) throws RemoteException {
        String apkfile = null;
        try {
            PackageManager pm = getPackageManager();
            PackageInfo info = pm.getPackageArchiveInfo(filepath, 0);
            if (info == null) {
                Log.e(TAG, "installPackage info == null");
                return null;
            }
//            apkfile = DirHelper.getPluginApkFile(this, info.packageName);
//            new File(apkfile).delete();

//            //cp plug apk to data/user/0/{package name}/Plugin/{plug apk package name}/apk/base-1.apk
//            Utils.copyFile(filepath, apkfile);
//
            //if need backup plug apk, change filepath to apkfile
            PluginPackageParser parser = new PluginPackageParser(this, new File(filepath));
            parser.collectCertificates(0);
            if (copyNativeLibs(this, filepath, parser.getApplicationInfo(0)) < 0) {
//                new File(apkfile).delete();
                Log.e(TAG, "installPackage copyNativeLibs == null");
                return null;
            }
            return info;
        }catch (Exception e){
            Log.e(TAG, "installPackage Exception", e);
            e.printStackTrace();
        }
        return  null;
    }

    private int copyNativeLibs(Context context, String apkfile, ApplicationInfo applicationInfo) throws Exception {
        String nativeLibraryDir = DirHelper.getPluginNativeLibraryDir(context, applicationInfo.packageName);
        Log.i(TAG, "copyNativeLibs to :" + nativeLibraryDir);
        return NativeLibraryHelperCompat.copyNativeBinaries(new File(apkfile), new File(nativeLibraryDir));
    }
}
