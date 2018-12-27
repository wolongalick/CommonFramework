package common.permission;

/**
 * Created by cxw on 2016/11/9.
 */
public interface IPermission {
    void requestPermissionGroup(String... permissionNames);

    void requestStorage(Object...params);

    void requestCamera(Object...params);

    void requestMicrophone(Object...params);

    void requestPhone(Object...params);

    void requestLocation(Object...params);

    void requestContacts(Object...params);

    void requestCalendar(Object...params);

    void requestSMS(Object...params);

    void requestSenors(Object...params);

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
