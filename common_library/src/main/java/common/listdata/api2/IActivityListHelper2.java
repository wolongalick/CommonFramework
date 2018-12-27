package common.listdata.api2;

import android.app.Activity;

import common.base.adapter.BasicRecyclerAdapter;
import common.base.viewholder.BaseViewHolder;

/**
 * Created by Alick on 2016/9/25.
 */

public interface IActivityListHelper2<Modeal,Holder extends BaseViewHolder, Adapter extends BasicRecyclerAdapter<Modeal,Holder>> extends IViewListHelper2<Modeal,Holder, Adapter>{
    /**
     * 初始化控件
     * @param activity
     */
    void initWidgets(Activity activity);
}
