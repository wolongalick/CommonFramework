package common.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;

/**
 * Created by Alick on 2015/12/21.
 */
public class PhoneUtils {
    private static TelephonyManager tm;

    public static String getImei(Context context){
        try {
            if(ContextCompat.checkSelfPermission(context,Manifest.permission.READ_PHONE_STATE)!=PackageManager.PERMISSION_GRANTED){
                return "";
            }
            if(tm==null){
                tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            }
            if(tm!=null){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    return tm.getImei();
                }else {
                    String deviceId = tm.getDeviceId();
                    if(deviceId==null){
                        deviceId="";
                    }
                    return deviceId;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}

