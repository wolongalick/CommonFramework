package common.utils;

import android.text.TextUtils;
import android.util.Log;


public class BLog {

    public static String TAG = "cxw_Joke";

    /**
     * 日志总开关
     */
    public static boolean rootLogOn = true;

    public static final String COLON = ": ";

    public static final boolean DEBUGV = true;

    // public static final boolean DEBUGD = Log.isLoggable(CotaLog.TAG, Log.DEBUG);
    public static final boolean DEBUGD = true;

    public static final boolean DEBUGI = true;

    public static final boolean DEBUGW = true;

    public static void setTag(String tag) {
        TAG = tag;
    }

    /**
     * 设置日志总开关
     *
     * @param bOn 日志开关
     */
    public static void setLogOn(boolean bOn) {
        rootLogOn = bOn;
    }

    public static void v(String tag, String msg) {
        if (DEBUGV) {
            Log.v(TAG, tag + COLON + msg);
        }
    }
    public static void v(String msg) {
        if (rootLogOn && DEBUGI) {
            v("", msg);
        }
    }

    public static void i(String msg) {
        if (rootLogOn && DEBUGI) {
            i("", msg);
        }
    }


    public static void i(String tag, String msg) {
        if (rootLogOn && DEBUGI) {
            if (!TextUtils.isEmpty(tag)) {
                Log.i(TAG, tag + COLON + msg);
            } else {
                Log.i(TAG, msg);
            }
        }
    }

    public static void d(String msg) {
        if (rootLogOn && DEBUGD) {
            d("", msg);
        }
    }

    public static void d(String tag, String msg) {
        if (rootLogOn && DEBUGI) {
            if (!TextUtils.isEmpty(tag)) {
                Log.d(TAG, tag + COLON + msg);
            } else {
                Log.d(TAG, msg);
            }
        }
    }

    public static void w(String tag, String msg) {
        if (rootLogOn && DEBUGW) {
            Log.w(TAG, tag + COLON + msg);
        }
    }

    public static void w(String msg) {
        if (rootLogOn && DEBUGW) {
            w("", msg);
        }
    }

    public static void e(String tag, String msg) {
        Log.e(TAG, tag + COLON + msg);
    }

    public static void e(String msg) {
        if (rootLogOn && DEBUGI) {
            e("", msg);
        }
    }

    public static void e(String tag, String msg, Exception e) {
        Log.e(TAG, tag + COLON + msg, e);
    }
}
