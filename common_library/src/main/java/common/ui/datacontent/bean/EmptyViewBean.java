package common.ui.datacontent.bean;


/**
 * Created by Alick on 2016/3/19.
 */
public class EmptyViewBean {
    public enum ViewType {
        only_image,
        image_and_button
    }

//    private ViewType viewType;
    private int emptyImageResId;
    private String btnText;
    private boolean isClickable;

    public EmptyViewBean(int emptyImageResId) {
        this.emptyImageResId = emptyImageResId;
    }
    public EmptyViewBean(int emptyImageResId,boolean isClickable) {
        this.emptyImageResId = emptyImageResId;
        this.isClickable=isClickable;
    }

    public EmptyViewBean(int emptyImageResId, String btnText) {
        this.emptyImageResId = emptyImageResId;
        this.btnText = btnText;
    }

    public int getEmptyImageResId() {
        return emptyImageResId;
    }

    public void setEmptyImageResId(int emptyImageResId) {
        this.emptyImageResId = emptyImageResId;
    }

    public String getBtnText() {
        return btnText;
    }

    public void setBtnText(String btnText) {
        this.btnText = btnText;
    }

    public boolean isClickable() {
        return isClickable;
    }

    public void setIsClickable(boolean isClickable) {
        this.isClickable = isClickable;
    }
}
