package common.listdata.impl2;

import android.app.Activity;

import com.common.R;

import common.base.adapter.BasicRecyclerAdapter;
import common.base.viewholder.BaseViewHolder;
import common.listdata.api2.IActivityListHelper2;

/**
 * Created by Alick on 2016/9/25.
 */

public abstract class ActivityListHelperImpl2<Modeal,Holder extends BaseViewHolder, Adapter extends BasicRecyclerAdapter<Modeal,Holder>> extends ViewListHelperImpl2<Modeal,Holder, Adapter> implements IActivityListHelper2<Modeal,Holder, Adapter> {
    /**
     * 初始化控件
     * @param activity
     */
    @Override
    public void initWidgets(Activity activity){
        simpleGlobalFrameLayout2 = activity.findViewById(R.id.globalFrameLayout);
        smartRefreshLayout = simpleGlobalFrameLayout2.findViewById(R.id.refreshLayout);
        recyclerView = simpleGlobalFrameLayout2.findViewById(R.id.recyclerView);
        initListener();
    }

}
