package common.listdata.api2;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import common.base.BasicAdapter;
import common.base.BasicRecyclerAdapter;
import common.listdata.api1.IViewListHelper;

/**
 * Created by Alick on 2016/9/25.
 */

public interface IFragmentListHelper2<Model,Holder extends RecyclerView.ViewHolder, Adapter extends BasicRecyclerAdapter<Model,Holder>> extends IViewListHelper2<Model,Holder, Adapter>{
    /**
     * 初始化控件
     * @param rootView
     */
    void initWidgets(View rootView);
}
