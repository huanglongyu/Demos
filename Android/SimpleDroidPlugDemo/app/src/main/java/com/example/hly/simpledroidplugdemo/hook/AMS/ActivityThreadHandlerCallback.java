package com.example.hly.simpledroidplugdemo.hook.AMS;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.hly.simpledroidplugdemo.hook.DirHelper;
import com.example.hly.simpledroidplugdemo.hook.PMS.IPackageManagerHookHandler;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by hly on 9/20/16.
 */
public class ActivityThreadHandlerCallback implements Handler.Callback{

    Handler mBase;

    public ActivityThreadHandlerCallback(Handler base) {
        mBase = base;
    }

    @Override
    public boolean handleMessage(Message msg) {

        switch (msg.what) {
            // ActivityThread里面 "LAUNCH_ACTIVITY" 这个字段的值是100
            // 本来使用反射的方式获取最好, 这里为了简便直接使用硬编码
            case 100:
                handleLaunchActivity(msg);
//                Log.i(MainActivity.TAG, "handleMessage");
                break;
        }

        mBase.handleMessage(msg);
        return true;
    }

    private void handleLaunchActivity(Message msg) {
        // 这里简单起见,直接取出TargetActivity;

        Object obj = msg.obj;
        // 根据源码:
        // 这个对象是 ActivityClientRecord 类型
        // 我们修改它的intent字段为我们原来保存的即可.
/*        switch (msg.what) {
/             case LAUNCH_ACTIVITY: {
/                 Trace.traceBegin(Trace.TRACE_TAG_ACTIVITY_MANAGER, "activityStart");
/                 final ActivityClientRecord r = (ActivityClientRecord) msg.obj;
/
/                 r.packageInfo = getPackageInfoNoCheck(
/                         r.activityInfo.applicationInfo, r.compatInfo);
/                 handleLaunchActivity(r, null);
*/

        try {
            // 把替身恢复成真身
            Field intent = obj.getClass().getDeclaredField("intent");
            intent.setAccessible(true);
            Intent raw = (Intent) intent.get(obj);
            Intent target = raw.getParcelableExtra(DirHelper.EXTRA_TARGET_INTENT);
//            Intent target = new Intent();
//            target.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            target.setAction(Intent.ACTION_MAIN);
//            target.setComponent(new ComponentName("com.ludashi.vrbench","com.ludashi.vrbench.benchmark.bench.BenchmarkSetupActivity"));
            raw.setComponent(target.getComponent());

            Field activityInfoField = obj.getClass().getDeclaredField("activityInfo");
            activityInfoField.setAccessible(true);
            // 根据 getPackageInfo 根据这个 包名获取 LoadedApk的信息; 因此这里我们需要手动填上, 从而能够命中缓存
            ActivityInfo activityInfo = (ActivityInfo) activityInfoField.get(obj);

            activityInfo.applicationInfo.packageName = target.getPackage() == null ?
                    target.getComponent().getPackageName() : target.getPackage();
            hookPackageManager();

        } catch (NoSuchFieldException e) {
            Log.i("longyu", "NoSuchFieldException", e);
        } catch (IllegalAccessException e) {
            Log.i("longyu", "IllegalAccessException", e);
        } catch (Exception e) {
            Log.i("longyu", "Exception", e);
        }
    }

    private static void hookPackageManager() throws Exception {

        // 这一步是因为 initializeJavaContextClassLoader 这个方法内部无意中检查了这个包是否在系统安装
        // 如果没有安装, 直接抛出异常, 这里需要临时Hook掉 PMS, 绕过这个检查.

        Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
        Method currentActivityThreadMethod = activityThreadClass.getDeclaredMethod("currentActivityThread");
        currentActivityThreadMethod.setAccessible(true);
        Object currentActivityThread = currentActivityThreadMethod.invoke(null);

        // 获取ActivityThread里面原始的 sPackageManager
        Field sPackageManagerField = activityThreadClass.getDeclaredField("sPackageManager");
        sPackageManagerField.setAccessible(true);
        Object sPackageManager = sPackageManagerField.get(currentActivityThread);

        // 准备好代理对象, 用来替换原始的对象
        Class<?> iPackageManagerInterface = Class.forName("android.content.pm.IPackageManager");
        Object proxy = Proxy.newProxyInstance(iPackageManagerInterface.getClassLoader(),
                new Class<?>[] { iPackageManagerInterface },
                new IPackageManagerHookHandler(sPackageManager));

        // 1. 替换掉ActivityThread里面的 sPackageManager 字段
        sPackageManagerField.set(currentActivityThread, proxy);

    }
}
