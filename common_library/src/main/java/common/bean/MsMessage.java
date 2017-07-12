package common.bean;

public class MsMessage {
    private String err_cd;

    private String err_desc;

    private String hint;

    private Object data;

    public void setErr_cd(String err_cd) {
        this.err_cd = err_cd;
    }

    public String getErr_cd() {
        return this.err_cd;
    }

    public void setErr_desc(String err_desc) {
        this.err_desc = err_desc;
    }

    public String getErr_desc() {
        return this.err_desc;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getHint() {
        return this.hint;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getData() {
        return this.data;
    }

}
