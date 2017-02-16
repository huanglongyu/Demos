package com.example.hly.simpledroidplugdemo.hook;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.ArrayMap;
import android.util.Log;

import com.example.hly.simpledroidplugdemo.MainActivity;

import java.io.File;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import dalvik.system.DexClassLoader;

/**
 * Created by hly on 9/20/16.
 */
public class ClassLoaderHookHelper {
    public static Map<String, Object> sLoadedApk = new HashMap<String, Object>();

    public static void hookLoadedApkInActivityThread(Context c, File apkFile) throws ClassNotFoundException,
            NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException, InstantiationException {

        Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
        Method currentActivityThreadMethod = activityThreadClass.getDeclaredMethod("currentActivityThread");
        currentActivityThreadMethod.setAccessible(true);
        Object currentActivityThread = currentActivityThreadMethod.invoke(null);

        // 获取到 mPackages 这个静态成员变量, 这里缓存了dex包的信息
        Field mPackagesField = activityThreadClass.getDeclaredField("mPackages");
        mPackagesField.setAccessible(true);
        ArrayMap mPackages = (ArrayMap) mPackagesField.get(currentActivityThread);

        for (int i = 0; i < mPackages.size(); i++) {
            Log.i(MainActivity.TAG, "raw mPackages[" + i + "] :" + mPackages.keyAt(i));
        }

        // android.content.res.CompatibilityInfo
        Class<?> compatibilityInfoClass = Class.forName("android.content.res.CompatibilityInfo");
        Method getPackageInfoNoCheckMethod = activityThreadClass.getDeclaredMethod("getPackageInfoNoCheck", ApplicationInfo.class, compatibilityInfoClass);

        Field defaultCompatibilityInfoField = compatibilityInfoClass.getDeclaredField("DEFAULT_COMPATIBILITY_INFO");
        defaultCompatibilityInfoField.setAccessible(true);

        Object defaultCompatibilityInfo = defaultCompatibilityInfoField.get(null);
        ApplicationInfo applicationInfo = generateApplicationInfo(apkFile);

//        Log.i(MainActivity.TAG, "target ApplicationInfo: " + applicationInfo.sourceDir + " " + applicationInfo.nativeLibraryDir + " "
//                +applicationInfo.packageName);

        Object loadedApk = getPackageInfoNoCheckMethod.invoke(currentActivityThread, applicationInfo, defaultCompatibilityInfo);

        String optimizedDirectory = DirHelper.getPluginDalvikCacheDir(c, applicationInfo.packageName);
        String libraryPath = DirHelper.getPluginNativeLibraryDir(c, applicationInfo.packageName);
        ClassLoader classLoader = new DexClassLoader(apkFile.getPath(), optimizedDirectory, libraryPath, ClassLoader.getSystemClassLoader());
        Log.i(MainActivity.TAG, "Plug apk added, dexPath:" + apkFile.getPath() + " optimizedDirectory:" + optimizedDirectory + " libraryPath:" + libraryPath);
        Field mClassLoaderField = loadedApk.getClass().getDeclaredField("mClassLoader");
        mClassLoaderField.setAccessible(true);
        mClassLoaderField.set(loadedApk, classLoader);

        // 由于是弱引用, 因此我们必须在某个地方存一份, 不然容易被GC; 那么就前功尽弃了.
        sLoadedApk.put(applicationInfo.packageName, loadedApk);

        WeakReference weakReference = new WeakReference(loadedApk);
        mPackages.put(applicationInfo.packageName, weakReference);

        for (int i = 0; i < mPackages.size(); i++) {
            Log.i(MainActivity.TAG, "after add new dexclassloader mPackages[" + i + "] :" + mPackages.keyAt(i));
        }
    }

    /**
     * 这个方法的最终目的是调用
     * android.content.pm.PackageParser#generateActivityInfo(android.content.pm.PackageParser.Activity, int, android.content.pm.PackageUserState, int)
     */
    public static ApplicationInfo generateApplicationInfo(File apkFile)
            throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchFieldException {


        // 找出需要反射的核心类: android.content.pm.PackageParser
        Class<?> packageParserClass = Class.forName("android.content.pm.PackageParser");

        // 我们的终极目标: android.content.pm.PackageParser#generateApplicationInfo(android.content.pm.PackageParser.Package,
        // int, android.content.pm.PackageUserState)
        // 要调用这个方法, 需要做很多准备工作; 考验反射技术的时候到了 - -!
        // 下面, 我们开始这场Hack之旅吧!

        // 首先拿到我们得终极目标: generateApplicationInfo方法
        // API 23 !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        // public static ApplicationInfo generateApplicationInfo(Package p, int flags,
        //    PackageUserState state) {
        // 其他Android版本不保证也是如此.
        Class<?> packageParser$PackageClass = Class.forName("android.content.pm.PackageParser$Package");
        Class<?> packageUserStateClass = Class.forName("android.content.pm.PackageUserState");
        Method generateApplicationInfoMethod = packageParserClass.getDeclaredMethod("generateApplicationInfo",
                packageParser$PackageClass,
                int.class,
                packageUserStateClass);

        // 接下来构建需要得参数

        // 首先, 我们得创建出一个Package对象出来供这个方法调用
        // 而这个需要得对象可以通过 android.content.pm.PackageParser#parsePackage 这个方法返回得 Package对象得字段获取得到
        // 创建出一个PackageParser对象供使用
        Object packageParser = packageParserClass.newInstance();
        // 调用 PackageParser.parsePackage 解析apk的信息
        Method parsePackageMethod = packageParserClass.getDeclaredMethod("parsePackage", File.class, int.class);

        // 实际上是一个 android.content.pm.PackageParser.Package 对象
        Object packageObj = parsePackageMethod.invoke(packageParser, apkFile, 0);

        // 第三个参数 mDefaultPackageUserState 我们直接使用默认构造函数构造一个出来即可
        Object defaultPackageUserState = packageUserStateClass.newInstance();

        // 万事具备!!!!!!!!!!!!!!
        ApplicationInfo applicationInfo = (ApplicationInfo) generateApplicationInfoMethod.invoke(packageParser,
                packageObj, 0, defaultPackageUserState);
        String apkPath = apkFile.getPath();

        applicationInfo.sourceDir = apkPath;
        applicationInfo.publicSourceDir = apkPath;

        return applicationInfo;
    }
}
