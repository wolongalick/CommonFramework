package common.ui;

import android.view.View;

/**
 * 通用的列表项点击监听
 * Created by cxw on 2016/11/17.
 */

public interface ICommonOnItemClickListener<Holder,Model> {
    /**
     * 点击列表项时触发的回调函数
     * @param position
     */
    void onItemClick(Holder holder,Model model, int position);
}
