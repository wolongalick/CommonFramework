package common.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;

/**
 * Created by Alick on 2015/12/21.
 */
public class PhoneUtils {
    private static TelephonyManager tm;


    public static String getDeviceId(Context context){
        if(ContextCompat.checkSelfPermission(context,Manifest.permission.READ_PHONE_STATE)!=PackageManager.PERMISSION_GRANTED){
            return "";
        }
        if(tm==null){
            tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        }
        return tm.getDeviceId();
    }
}

