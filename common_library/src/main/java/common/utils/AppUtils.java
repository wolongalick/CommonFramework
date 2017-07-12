package common.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;
import java.util.List;

//跟App相关的辅助类
public class AppUtils {

    private static final java.lang.String TAG = AppUtils.class.getSimpleName();

    private AppUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");

    }

    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * [获取应用程序版本名称信息]
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;

        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * [获取应用程序版本号信息]
     *
     * @param context
     * @return 当前应用的版本号
     */
    public static int getVersionCode(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionCode;

        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 判断是否在后台
     * 弃用原因:如果有Service被设置成了START_STICKY,则appProcess.importance值永远为100,代表在前台运行
     *
     * @param context
     * @return
     */
    @Deprecated
    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    Log.i("后台", appProcess.processName);
                    return true;
                } else {
                    Log.i("前台", appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * 判断是否在前台
     * 弃用原因:如果有Service被设置成了START_STICKY,则appProcess.importance值永远为100,代表在前台运行
     *
     * @param context
     * @return
     */
    @Deprecated
    public static boolean isForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    Log.i("前台", appProcess.processName);
                    return true;
                } else {
                    Log.i("后台", appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }

    public static boolean isTopActivity(Activity activity) {
        return isTopActivity(activity, activity.getClass().getName());
    }

    public static boolean isTopActivity(Context context, String activitySimpleName) {
        if (context == null || (activitySimpleName == null || activitySimpleName.trim().length() == 0)) {
            return false;
        }

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
        if (tasksInfo.isEmpty()) {
            return false;
        }
        try {
            String shortClassName = tasksInfo.get(0).topActivity.getClassName();
            BLog.i("栈顶Activity名称:" + shortClassName);
            return activitySimpleName.contains(shortClassName);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void checkPermission(Context context) {
        PackageManager pm = context.getPackageManager();
        boolean permission = (PackageManager.PERMISSION_GRANTED == pm.checkPermission("android.permission.ACCESS_FINE_LOCATION", context.getPackageName()));
        if (permission) {
            T.showShort(context, "有这个权限");
        } else {
            T.showShort(context, "没有这个权限");
        }
    }

    public static Activity getCurrentActivity(Context context) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
        if (tasksInfo.isEmpty()) {
            return null;
        }
        String shortClassName = tasksInfo.get(0).topActivity.getClassName();
        Class aClass = Class.forName(shortClassName);
        return (Activity) aClass.newInstance();
    }

    public static boolean isRunningTop(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            //如果系统版本>=5.0,则强制认为不在栈顶;
            return false;
        }
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
        String packageName = tasksInfo.get(0).topActivity.getPackageName();
        BLog.i(TAG, "栈顶包名:" + packageName);
        return packageName.equals(context.getPackageName());
    }

    /**
     * 安装apk(已兼容Android7.0)
     *
     * @param context
     * @param filePath
     */
    public static void installApkFile(Context context, String filePath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(UriUtils.getUriCompatibleN(context, filePath), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }
}
