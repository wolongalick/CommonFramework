package common.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast统一管理类
 */
public class T {
    // Toast
    private static Toast toast;
    private static String lastMessage;              //上一个消息内容
    private static long lastTimestamp;              //上一个消息时间
    private static final long mininterval=1000;     //两个相邻的消息展示最少间隔时间

    public static void showShort(Context context, int messageRes) {
        showShort(context, context.getString(messageRes));
    }

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, CharSequence message) {
        if (context == null) {
            return;
        }
        long currentTimestamp = System.currentTimeMillis();
        if (toast == null || message == null || !message.equals(lastMessage) || currentTimestamp - lastTimestamp > mininterval) {
            toast = Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_SHORT);
        } else {
            toast.setText(message);
        }
        if (message != null) {
            lastMessage = message.toString();
        } else {
            lastMessage = "";
        }
        lastTimestamp = currentTimestamp;
        toast.show();
    }


    public static void showLong(Context context, int messageRes) {
        showLong(context, context.getString(messageRes));
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, CharSequence message) {
        if (context == null) {
            return;
        }
        long currentTimestamp = System.currentTimeMillis();
        if (toast == null || message == null || !message.equals(lastMessage) || currentTimestamp - lastTimestamp > mininterval) {
            toast = Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_LONG);
        } else {
            toast.setText(message);
        }
        if (message != null) {
            lastMessage = message.toString();
        } else {
            lastMessage = "";
        }
        lastTimestamp = currentTimestamp;
        toast.show();
    }

    /**
     * Hide the toast, if any.
     */
    public static void hideToast() {
        if (null != toast) {
            toast.cancel();
        }
    }
}
