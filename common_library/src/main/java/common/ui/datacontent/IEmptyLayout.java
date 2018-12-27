package common.ui.datacontent;

import android.support.annotation.DrawableRes;
import android.view.View;

/**
 * Created by Alick on 2015/10/12.
 */
public interface IEmptyLayout {

    interface OnClickEmptyLayoutListener {
        /**
         * 点击空数据页面上的按钮时触发
         * @param v
         */
        void onClick(View v);
    }

    void setEmptyView(View view);

    void setEmptyText(String hintText);

    void setEmptyImg(@DrawableRes int emptyImgResId);

    void setEmptyBtnText(String btnText);

    void setOnClickEmptyLayoutListener(OnClickEmptyLayoutListener onClickEmptyLayoutListener);


}
