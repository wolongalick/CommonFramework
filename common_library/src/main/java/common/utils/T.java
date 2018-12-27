package common.utils;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.common.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Toast统一管理类
 */
public class T {
    // Toast
    private static ToastCompat toast;
    private static String lastMessage;              //上一个消息内容
    private static long lastTimestamp;              //上一个消息时间
    private static final long mininterval=1000;     //两个相邻的消息展示最少间隔时间
    //public static boolean toastOn = true;          //toast开关



    public static void show(Context context, CharSequence message) {
        show(context,message,false);
    }

    public static void show(Context context, CharSequence message,CharSequence errorMessage) {
        if(BLog.rootLogOn){
            message=message+":"+errorMessage;
        }
        show(context,message,false);
    }




    public static void showLong(Context context, CharSequence message) {
        show(context,message,true);
    }

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    private static void show(Context context, CharSequence message,boolean isLong) {
        if (context == null) {
            return;
        }

        long currentTimestamp = System.currentTimeMillis();
        View layoutToast = LayoutInflater.from(context).inflate(R.layout.layout_toast, null);
        TextView tvToastText = layoutToast.findViewById(R.id.tv_toastText);

        if (toast == null || message == null || !message.equals(lastMessage) || currentTimestamp - lastTimestamp > mininterval) {
            if(context instanceof Activity){
                toast=new ToastCompat(context.getApplicationContext());
            }else {
                toast=new ToastCompat(context);
            }

            toast.setDuration(isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
        }
        tvToastText.setText(message);
        toast.setView(layoutToast);

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
    private static void hideToast() {
        if (null != toast) {
            toast.cancel();
        }
    }

    public static void showMyToast(final Toast toast, final int cnt) {
        final Timer timer =new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                toast.show();
            }
        },0,3000);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                toast.cancel();
                timer.cancel();
            }
        }, cnt );
    }
}
