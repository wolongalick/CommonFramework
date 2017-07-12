package common.bean;

/**
 * Created by cxw on 2016/2/16.
 */
public class ErrorMsg {
    private int code;
    private int errorCode;
    private String msg;
    private boolean isShowErrorEmpty;

    public ErrorMsg(int code, int errorCode, String msg) {
        this.code = code;
        this.errorCode = errorCode;
        this.msg = msg;
    }

    public ErrorMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ErrorMsg(String msg) {
        this.code = -1;
        this.msg = msg;
    }

    public ErrorMsg(int code, String msg, boolean isShowErrorEmpty) {
        this.code = code;
        this.msg = msg;
        this.isShowErrorEmpty = isShowErrorEmpty;
    }

    public ErrorMsg(int code, int errorCode, String msg, boolean isShowErrorEmpty) {
        this.code = code;
        this.msg = msg;
        this.errorCode = errorCode;
        this.isShowErrorEmpty = isShowErrorEmpty;
    }


    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isShowErrorEmpty() {
        return isShowErrorEmpty;
    }

    public void setIsShowErrorEmpty(boolean isShowErrorEmpty) {
        this.isShowErrorEmpty = isShowErrorEmpty;
    }
}
