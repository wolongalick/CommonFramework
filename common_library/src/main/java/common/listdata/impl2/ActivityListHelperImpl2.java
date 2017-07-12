package common.listdata.impl2;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;

import com.common.R;
import com.socks.autoload.AutoLoadListView;

import common.base.BasicRecyclerAdapter;
import common.listdata.api2.IActivityListHelper2;
import common.listdata.impl.ViewListHelperImpl;
import common.ui.datacontent.SimpleGlobalFrameLayout;
import common.ui.datacontent.SimpleGlobalFrameLayout2;

/**
 * Created by Alick on 2016/9/25.
 */

public abstract class ActivityListHelperImpl2<Modeal,Holder extends RecyclerView.ViewHolder, Adapter extends BasicRecyclerAdapter<Modeal,Holder>> extends ViewListHelperImpl2<Modeal,Holder, Adapter> implements IActivityListHelper2<Modeal,Holder, Adapter> {
    /**
     * 初始化控件
     * @param activity
     */
    @Override
    public void initWidgets(Activity activity){
        simpleGlobalFrameLayout2 = (SimpleGlobalFrameLayout2) activity.findViewById(R.id.globalFrameLayout);
        recyclerView = (RecyclerView) simpleGlobalFrameLayout2.findViewById(R.id.recyclerView);
        initListener();
    }

}
