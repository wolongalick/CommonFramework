package common.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;

import java.util.UUID;

/**
 * 设备工具类
 * Created by cxw on 2016/1/29.
 */
public class DeviceUtils {

    private static String uuid;
    private static String imei;
    private static String androidId;

    /**
     * 检查是否有虚拟键盘
     *
     * @param activity
     * @return
     */
    public static boolean checkDeviceHasNavigationBar(Context activity) {
        //通过判断设备是否有返回键、菜单键(不是虚拟键,是手机屏幕外的按键)来确定是否有navigation bar
        boolean hasMenuKey = ViewConfiguration.get(activity).hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
        // 做任何你需要做的,这个设备有一个导航栏
        return !hasMenuKey && !hasBackKey;
    }

    /**
     * 获取导航栏高度
     *
     * @param context
     * @return
     */
    public static int getNavigationBarHeight(Context context) {
        if(!DeviceUtils.checkDeviceHasNavigationBar(context)){
            return 0;
        }
        try {
            Resources resources = context.getResources();
            int resourceId = resources.getIdentifier("navigation_bar_height",
                    "dimen", "android");
            //获取NavigationBar的高度
            int height = resources.getDimensionPixelSize(resourceId);
            return height;
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    //获得独一无二的Psuedo ID
    public static String getUniquePsuedoID() {
        if(!TextUtils.isEmpty(uuid)){
            return uuid;
        }
        //先从配置文件中获取,如果不存在,再生成
        uuid = SpUtils.getInstance().get(CommonConstant.SPKeys.UUID.name(), "");
        if(!TextUtils.isEmpty(uuid)){
            return uuid;
        }

        String serial = null;
        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +

                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +

                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +

                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +

                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +

                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +

                Build.USER.length() % 10; //13 位

        try {
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();
        } catch (Exception exception) {
            //serial需要一个初始化
            serial = "serial"; // 随便一个初始化
        }
        //使用硬件信息拼凑出来的15位号码
        uuid=new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        //保存到配置文件
        SpUtils.getInstance().put(CommonConstant.SPKeys.UUID.name(),uuid);

        return uuid;
    }


    public static String getImei() {
        if(!TextUtils.isEmpty(imei)){
            return imei;
        }

        imei=SpUtils.getInstance().get(CommonConstant.SPKeys.IMEI.name(),"");

        if(!TextUtils.isEmpty(imei)){
            return imei;
        }

        return imei;
    }

    public static void saveImei(String imei){
        DeviceUtils.imei=imei;
        SpUtils.getInstance().put(CommonConstant.SPKeys.IMEI.name(),imei);
    }



    @SuppressLint("HardwareIds")
    public static String getAndroidID(Context context){
        if(!TextUtils.isEmpty(androidId)){
            return androidId;
        }

        androidId=SpUtils.getInstance().get(CommonConstant.SPKeys.ANDROID_ID.name(),"");

        if(!TextUtils.isEmpty(androidId)){
            return androidId;
        }

        if(context==null){
            return "";
        }

        androidId=Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        if(!TextUtils.isEmpty(androidId)){
            SpUtils.getInstance().put(CommonConstant.SPKeys.ANDROID_ID.name(),androidId);
        }

        return androidId;
    }


}
