package common.listdata.api2;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;

import common.base.BasicRecyclerAdapter;

/**
 * Created by Alick on 2016/9/25.
 */

public interface IActivityListHelper2<Modeal,Holder extends RecyclerView.ViewHolder, Adapter extends BasicRecyclerAdapter<Modeal,Holder>> extends IViewListHelper2<Modeal,Holder, Adapter>{
    /**
     * 初始化控件
     * @param activity
     */
    void initWidgets(Activity activity);
}
