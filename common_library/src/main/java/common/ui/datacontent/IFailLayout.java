package common.ui.datacontent;

import android.view.View;

/**
 * Created by Alick on 2015/10/12.
 */
public interface IFailLayout {
    interface OnClickReloadButtonListener{
        /**
         * 点击失败界面时触发的回调函数
         * @param v
         */
        void onClick(View v);
    }

    void setOnClickReloadButton(OnClickReloadButtonListener onClickReloadButtonListener);

    void setFailView(int failImageResId);





}
