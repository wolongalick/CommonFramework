package common.listdata.api1;

import android.view.View;

import common.base.BasicAdapter;

/**
 * Created by Alick on 2016/9/25.
 */

public interface IFragmentListHelper<Model, Adapter extends BasicAdapter<Model>> extends IViewListHelper<Model, Adapter>{
    /**
     * 初始化控件
     * @param rootView
     */
    void initWidgets(View rootView);
}
