package common.permission;

/**
 * Created by cxw on 2016/11/9.
 */

public class PermissionConstant {
    public static final int REQUEST_CODE_PERMISSION_GROUP = 100;
    public static final int REQUEST_CODE_STORAGE=0;
    public static final int REQUEST_CODE_CAMERA=1;
    public static final int REQUEST_CODE_MICROPHONE=2;
    public static final int REQUEST_CODE_PHONE=3;
    public static final int REQUEST_CODE_LOCATION=4;
    public static final int REQUEST_CODE_CONTACTS=5;
    public static final int REQUEST_CODE_CALENDAR=6;
    public static final int REQUEST_CODE_SMS=7;
    public static final int REQUEST_CODE_SENORS=8;

    public static final String REQUEST_HINT_STORAGE = "请授予app读取SD卡权限";
    public static final String REQUEST_HINT_CAMERA = "请授予app打开相机权限";
    public static final String REQUEST_HINT_MICROPHONE = "请授予app打开麦克风权限";
    public static final String REQUEST_HINT_PHONE = "请授予app获取电话信息权限";
    public static final String REQUEST_HINT_LOCATION = "请授予app获取地理位置权限";
    public static final String REQUEST_HINT_CONTACTS = "请授予app读取联系人权限";
    public static final String REQUEST_HINT_CALENDAR = "请授予app获取日历权限";
    public static final String REQUEST_HINT_SMS = "请授予app读取短信权限";
    public static final String REQUEST_HINT_SENORS = "请授予app使用传感器权限";
}
