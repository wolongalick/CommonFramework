package common.listdata.api2;

import android.view.View;

import common.base.adapter.BasicRecyclerAdapter;
import common.base.viewholder.BaseViewHolder;

/**
 * Created by Alick on 2016/9/25.
 */

public interface IFragmentListHelper2<Model,Holder extends BaseViewHolder, Adapter extends BasicRecyclerAdapter<Model,Holder>> extends IViewListHelper2<Model,Holder, Adapter>{
    /**
     * 初始化控件
     * @param rootView
     */
    void initWidgets(View rootView);
}
