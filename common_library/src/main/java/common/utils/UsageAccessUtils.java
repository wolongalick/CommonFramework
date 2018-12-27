package common.utils;

import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.provider.Settings;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by cxw on 2017/2/21.
 */

public class UsageAccessUtils {
    private static final int TIEM_LENGTH = 60 * 60 * 24;//单位:秒

    /**
     * 是否存在"有权查看应用使用情况"模块
     *
     * @return
     */
    public static boolean hasModule(Context context) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        }
        return list != null && list.size() > 0;
    }

    /**
     * 是否已经打开开关
     *
     * @return
     */
    public static boolean hasEnable(Context context) {
        long ts = System.currentTimeMillis();
        UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        List<UsageStats> queryUsageStats = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            queryUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, 0, ts);
        }
        return !(queryUsageStats == null || queryUsageStats.isEmpty());
    }

    /**
     * 高版本：获取顶层的activity的包名
     * @return
     */
    public static String getHigherVersionPackageName(Context context) {
        String topPackageName = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager mUsageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * TIEM_LENGTH, time);
            if (stats != null) {
                SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
                for (UsageStats usageStats : stats) {
                    mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                }
                if (mySortedMap != null && !mySortedMap.isEmpty()) {
                    topPackageName = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                }
            }
        }
        return topPackageName;
    }

    public static void gotoUsageActivity(Context context){
        //开启应用授权界面
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 低版本：获取栈顶app的包名
     *
     * @return
     */
    public static String getLowerVersionPackageName(Context context) {
        String topPackageName;//低版本  直接获取getRunningTasks
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName topActivity = activityManager.getRunningTasks(1).get(0).topActivity;
        topPackageName = topActivity.getPackageName();
        return topPackageName;
    }


}
