package common.permission;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import common.utils.T;

import static common.permission.PermissionConstant.REQUEST_CODE_CALENDAR;
import static common.permission.PermissionConstant.REQUEST_CODE_CAMERA;
import static common.permission.PermissionConstant.REQUEST_CODE_CONTACTS;
import static common.permission.PermissionConstant.REQUEST_CODE_LOCATION;
import static common.permission.PermissionConstant.REQUEST_CODE_MICROPHONE;
import static common.permission.PermissionConstant.REQUEST_CODE_PERMISSION_GROUP;
import static common.permission.PermissionConstant.REQUEST_CODE_PHONE;
import static common.permission.PermissionConstant.REQUEST_CODE_SENORS;
import static common.permission.PermissionConstant.REQUEST_CODE_SMS;
import static common.permission.PermissionConstant.REQUEST_CODE_STORAGE;
import static common.permission.PermissionConstant.REQUEST_HINT_CALENDAR;
import static common.permission.PermissionConstant.REQUEST_HINT_CAMERA;
import static common.permission.PermissionConstant.REQUEST_HINT_CONTACTS;
import static common.permission.PermissionConstant.REQUEST_HINT_LOCATION;
import static common.permission.PermissionConstant.REQUEST_HINT_MICROPHONE;
import static common.permission.PermissionConstant.REQUEST_HINT_PHONE;
import static common.permission.PermissionConstant.REQUEST_HINT_SENORS;
import static common.permission.PermissionConstant.REQUEST_HINT_SMS;
import static common.permission.PermissionConstant.REQUEST_HINT_STORAGE;

/**
 * Created by cxw on 2016/9/2.
 */
public abstract class BasePermissionActivity extends Activity implements OnPermissionListener, IPermission {

    @Override
    public void requestPermissionGroup(String... permissionNames){
        ActivityCompat.requestPermissions(this,permissionNames, REQUEST_CODE_PERMISSION_GROUP);
    }

    @Override
    public void requestStorage(){
        requestStorage(new PermissionBean(Manifest.permission.READ_EXTERNAL_STORAGE,REQUEST_CODE_STORAGE,REQUEST_HINT_STORAGE));
    }

    @Override
    public void requestCamera(){
        requestCamera(new PermissionBean(Manifest.permission.CAMERA, REQUEST_CODE_CAMERA,REQUEST_HINT_CAMERA));
    }

    @Override
    public void requestMicrophone(){
        requestMicrophone(new PermissionBean(Manifest.permission.RECORD_AUDIO,REQUEST_CODE_MICROPHONE,REQUEST_HINT_MICROPHONE));
    }

    @Override
    public void requestPhone(){
        requestPhone(new PermissionBean(Manifest.permission.READ_PHONE_STATE,REQUEST_CODE_PHONE,REQUEST_HINT_PHONE));
    }

    @Override
    public void requestLocation(){
        requestLocation(new PermissionBean(Manifest.permission.ACCESS_FINE_LOCATION,REQUEST_CODE_LOCATION,REQUEST_HINT_LOCATION));
    }

    @Override
    public void requestContacts(){
        requestContacts(new PermissionBean(Manifest.permission.READ_CONTACTS,REQUEST_CODE_CONTACTS,REQUEST_HINT_CONTACTS));
    }

    @Override
    public void requestCalendar(){
        requestCalendar(new PermissionBean(Manifest.permission.READ_CALENDAR,REQUEST_CODE_CALENDAR,REQUEST_HINT_CALENDAR));
    }

    @Override
    public void requestSMS(){
        requestSMS(new PermissionBean(Manifest.permission.READ_SMS,REQUEST_CODE_SMS,REQUEST_HINT_SMS));
    }

    @Override
    public void requestSenors(){
        requestSenors(new PermissionBean(Manifest.permission.BODY_SENSORS,REQUEST_CODE_SENORS,REQUEST_HINT_SENORS));
    }
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

    @Override
    public void requestStorage(String hint){
        requestStorage(new PermissionBean(Manifest.permission.READ_EXTERNAL_STORAGE,REQUEST_CODE_STORAGE,hint));
    }

    @Override
    public void requestCamera(String hint){
        requestCamera(new PermissionBean(Manifest.permission.CAMERA,REQUEST_CODE_CAMERA,hint));
    }

    @Override
    public void requestMicrophone(String hint){
        requestMicrophone(new PermissionBean(Manifest.permission.RECORD_AUDIO,REQUEST_CODE_MICROPHONE,hint));
    }

    @Override
    public void requestPhone(String hint){
        requestPhone(new PermissionBean(Manifest.permission.READ_PHONE_STATE,REQUEST_CODE_PHONE,hint));
    }

    @Override
    public void requestLocation(String hint){
        requestLocation(new PermissionBean(Manifest.permission.ACCESS_FINE_LOCATION,REQUEST_CODE_LOCATION,hint));
    }

    @Override
    public void requestContacts(String hint){
        requestContacts(new PermissionBean(Manifest.permission.READ_CONTACTS,REQUEST_CODE_CONTACTS,hint));
    }

    @Override
    public void requestCalendar(String hint){
        requestCalendar(new PermissionBean(Manifest.permission.READ_CALENDAR,REQUEST_CODE_CALENDAR,hint));
    }

    @Override
    public void requestSMS(String hint){
        requestSMS(new PermissionBean(Manifest.permission.READ_SMS,REQUEST_CODE_SMS,hint));
    }

    @Override
    public void requestSenors(String hint){
        requestSenors(new PermissionBean(Manifest.permission.BODY_SENSORS,REQUEST_CODE_SENORS,hint));
    }

    /**
     * 检查权限
     * @param permissionBean
     * @return
     */
    private boolean checkPermission(PermissionBean permissionBean){
        return ContextCompat.checkSelfPermission(this,permissionBean.getPermissionName()) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 显示申请权限时弹出的提示信息(如果需要)
     * @param permissionBean
     */
    private void showPermissionHintIfNeed(PermissionBean permissionBean){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissionBean.getPermissionName())) {
            T.showShort(this,permissionBean.getRequetHint());
        }
    }

    /**
     * 申请权限
     * @param permissionBean
     */
    private void requestPermission(PermissionBean permissionBean){
        ActivityCompat.requestPermissions(this, new String[]{permissionBean.getPermissionName()}, permissionBean.getRequestCode());
    }

    //********************************************************************************

    private void requestStorage(PermissionBean permissionBean){
        if (checkPermission(permissionBean)) {
            onGetStoragePerm(true);
        }else {
            showPermissionHintIfNeed(permissionBean);
            requestPermission(permissionBean);
        }
    }

    private void requestCamera(PermissionBean permissionBean){
        if (checkPermission(permissionBean)) {
            onGetCameraPerm(true);
        }else {
            showPermissionHintIfNeed(permissionBean);
            requestPermission(permissionBean);
        }
    }

    private void requestMicrophone(PermissionBean permissionBean){
        if (checkPermission(permissionBean)) {
            onGetMicrophonePerm(true);
        }else {
            showPermissionHintIfNeed(permissionBean);
            requestPermission(permissionBean);
        }
    }

    private void requestPhone(PermissionBean permissionBean){
        if (checkPermission(permissionBean)) {
            onGetPhonePerm(true);
        }else {
            showPermissionHintIfNeed(permissionBean);
            requestPermission(permissionBean);
        }
    }

    private void requestLocation(PermissionBean permissionBean){
        if (checkPermission(permissionBean)) {
            onGetLocationPerm(true);
        }else {
            showPermissionHintIfNeed(permissionBean);
            requestPermission(permissionBean);
        }
    }

    private void requestContacts(PermissionBean permissionBean){
        if (checkPermission(permissionBean)) {
            onGetContactsPerm(true);
        }else {
            showPermissionHintIfNeed(permissionBean);
            requestPermission(permissionBean);
        }
    }

    private void requestCalendar(PermissionBean permissionBean){
        if (checkPermission(permissionBean)) {
            onGetCalendarPerm(true);
        }else {
            showPermissionHintIfNeed(permissionBean);
            requestPermission(permissionBean);
        }
    }

    private void requestSMS(PermissionBean permissionBean){
        if (checkPermission(permissionBean)) {
            onGetSmsPerm(true);
        }else {
            showPermissionHintIfNeed(permissionBean);
            requestPermission(permissionBean);
        }
    }

    private void requestSenors(PermissionBean permissionBean){
        if (checkPermission(permissionBean)) {
            onGetSenorsPerm(true);
        }else {
            showPermissionHintIfNeed(permissionBean);
            requestPermission(permissionBean);
        }
    }

    //===========================================================================================


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(permissions.length>1){
            switch (requestCode){
                case REQUEST_CODE_PERMISSION_GROUP:
                    List<String> result=new ArrayList<>();
                    for (int i = 0; i < permissions.length; i++) {
                        if(grantResults[i]==PackageManager.PERMISSION_DENIED){
                            result.add(permissions[i]);
                        }
                    }
                    onGetMultPermission(result);
                    break;
            }
        }else{
            switch (requestCode) {
                case REQUEST_CODE_STORAGE:
                    onGetStoragePerm(grantResults[0] == PackageManager.PERMISSION_GRANTED);
                    break;
                case REQUEST_CODE_CAMERA:
                    onGetCameraPerm(grantResults[0] == PackageManager.PERMISSION_GRANTED);
                    break;
                case REQUEST_CODE_MICROPHONE:
                    onGetMicrophonePerm(grantResults[0] == PackageManager.PERMISSION_GRANTED);
                    break;
                case REQUEST_CODE_PHONE:
                    onGetPhonePerm(grantResults[0] == PackageManager.PERMISSION_GRANTED);
                    break;
                case REQUEST_CODE_LOCATION:
                    onGetLocationPerm(grantResults[0] == PackageManager.PERMISSION_GRANTED);
                    break;
                case REQUEST_CODE_CONTACTS:
                    onGetContactsPerm(grantResults[0] == PackageManager.PERMISSION_GRANTED);
                    break;
                case REQUEST_CODE_CALENDAR:
                    onGetCalendarPerm(grantResults[0] == PackageManager.PERMISSION_GRANTED);
                    break;
                case REQUEST_CODE_SMS:
                    onGetSmsPerm(grantResults[0] == PackageManager.PERMISSION_GRANTED);
                    break;
                case REQUEST_CODE_SENORS:
                    onGetSenorsPerm(grantResults[0] == PackageManager.PERMISSION_GRANTED);
                    break;
            }
        }
    }

    /**
     * 当获得[多个权限]后的回调函数
     *
     * @param deniedResult
     */
    @Override
    public void onGetMultPermission(List<String> deniedResult) {

    }

    /**
     * 当获得[读写sd卡]权限后的回调函数
     *
     * @param isSuccessed
     */
    @Override
    public void onGetStoragePerm(boolean isSuccessed) {

    }

    /**
     * 当获得[使用相机]权限后的回调函数
     *
     * @param isSuccessed
     */
    @Override
    public void onGetCameraPerm(boolean isSuccessed) {

    }

    /**
     * 当获得[使用麦克风]权限后的回调函数
     *
     * @param isSuccessed
     */
    @Override
    public void onGetMicrophonePerm(boolean isSuccessed) {

    }

    /**
     * 当获得[获取电话信息、拨打电话]权限后的回调函数
     *
     * @param isSuccessed
     */
    @Override
    public void onGetPhonePerm(boolean isSuccessed) {

    }

    /**
     * 当获得[地理位置]权限后的回调函数
     *
     * @param isSuccessed
     */
    @Override
    public void onGetLocationPerm(boolean isSuccessed) {

    }

    /**
     * 当获得[读取联系人]权限后的回调函数
     *
     * @param isSuccessed
     */
    @Override
    public void onGetContactsPerm(boolean isSuccessed) {

    }

    /**
     * 当获得[读写日历]权限后的回调函数
     *
     * @param isSuccessed
     */
    @Override
    public void onGetCalendarPerm(boolean isSuccessed) {

    }

    /**
     * 当获得[收发短信]权限后的回调函数
     *
     * @param isSuccessed
     */
    @Override
    public void onGetSmsPerm(boolean isSuccessed) {

    }

    /**
     * 当获得[传感器]权限后的回调函数
     *
     * @param isSuccessed
     */
    @Override
    public void onGetSenorsPerm(boolean isSuccessed) {

    }
}
