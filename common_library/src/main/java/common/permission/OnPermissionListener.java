package common.permission;


import java.util.List;

/**
 * 权限接口,包含android中的全部权限(9个)
 * Created by cxw on 2016/9/2.
 */
public interface OnPermissionListener {
    /**
     * 当获得[多个权限]后的回调函数
     * @param deniedResult
     */
    void onGetMultPermission(List<String> deniedResult);

    /**
     * 当获得[读写sd卡]权限后的回调函数
     * @param isSuccessed
     */
    void onGetStoragePerm(boolean isSuccessed,Object...params);
    /**
     * 当获得[使用相机]权限后的回调函数
     * @param isSuccessed
     */
    void onGetCameraPerm(boolean isSuccessed,Object...params);
    /**
     * 当获得[使用麦克风]权限后的回调函数
     * @param isSuccessed
     */
    void onGetMicrophonePerm(boolean isSuccessed,Object...params);
    /**
     * 当获得[获取电话信息、拨打电话]权限后的回调函数
     * @param isSuccessed
     */
    void onGetPhonePerm(boolean isSuccessed,Object...params);
    /**
     * 当获得[地理位置]权限后的回调函数
     * @param isSuccessed
     */
    void onGetLocationPerm(boolean isSuccessed,Object...params);
    /**
     * 当获得[读取联系人]权限后的回调函数
     * @param isSuccessed
     */
    void onGetContactsPerm(boolean isSuccessed,Object...params);
    /**
     * 当获得[读写日历]权限后的回调函数
     * @param isSuccessed
     */
    void onGetCalendarPerm(boolean isSuccessed,Object...params);
    /**
     * 当获得[收发短信]权限后的回调函数
     * @param isSuccessed
     */
    void onGetSmsPerm(boolean isSuccessed,Object...params);
    /**
     * 当获得[传感器]权限后的回调函数
     * @param isSuccessed
     */
    void onGetSenorsPerm(boolean isSuccessed,Object...params);
}
