package common.permission;

/**
 * Created by cxw on 2016/9/2.
 */
public class PermissionBean {
    private String permissionName;
    private int requestCode;
    private String requetHint;

    public PermissionBean(String permissionName, int requestCode, String requetHint) {
        this.permissionName = permissionName;
        this.requestCode = requestCode;
        this.requetHint = requetHint;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public String getRequetHint() {
        return requetHint;
    }

    public void setRequetHint(String requetHint) {
        this.requetHint = requetHint;
    }

    @Override
    public String toString() {
        return "Permission{" +
                "permissionName=" + permissionName +
                ", requestCode=" + requestCode +
                ", requetHint=" + requetHint +
                '}';
    }
}
