package common.permission;

/**
 * Created by cxw on 2016/11/9.
 */
public interface IPermission {
    void requestPermissionGroup(String... permissionNames);

    void requestStorage();

    void requestCamera();

    void requestMicrophone();

    void requestPhone();

    void requestLocation();

    void requestContacts();

    void requestCalendar();

    void requestSMS();

    void requestSenors();

    void requestStorage(String hint);

    void requestCamera(String hint);

    void requestMicrophone(String hint);

    void requestPhone(String hint);

    void requestLocation(String hint);

    void requestContacts(String hint);

    void requestCalendar(String hint);

    void requestSMS(String hint);

    void requestSenors(String hint);
}
