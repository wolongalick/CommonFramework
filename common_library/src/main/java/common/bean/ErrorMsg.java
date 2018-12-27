package common.bean;

/**
 * Created by cxw on 2016/2/16.
 */
public class ErrorMsg {
    private String code;
    private String msg;

    public ErrorMsg(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ErrorMsg(String msg) {
        this.code = "-1";
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
