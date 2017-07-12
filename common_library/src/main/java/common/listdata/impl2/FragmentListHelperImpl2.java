package common.listdata.impl2;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.common.R;
import com.socks.autoload.AutoLoadListView;

import common.base.BasicAdapter;
import common.base.BasicRecyclerAdapter;
import common.listdata.api1.IFragmentListHelper;
import common.listdata.api2.IFragmentListHelper2;
import common.listdata.impl.ViewListHelperImpl;
import common.ui.datacontent.SimpleGlobalFrameLayout;
import common.ui.datacontent.SimpleGlobalFrameLayout2;

/**
 * Created by Alick on 2016/9/25.
 */

public abstract class FragmentListHelperImpl2<Model,Holder extends RecyclerView.ViewHolder, Adapter extends BasicRecyclerAdapter<Model,Holder>> extends ViewListHelperImpl2<Model,Holder, Adapter> implements IFragmentListHelper2<Model,Holder, Adapter> {
    /**
     * 初始化控件
     * @param rootView
     */
    @Override
    public void initWidgets(View rootView){
        simpleGlobalFrameLayout2 = (SimpleGlobalFrameLayout2) rootView.findViewById(R.id.globalFrameLayout);
        recyclerView = (RecyclerView) simpleGlobalFrameLayout2.findViewById(R.id.recyclerView);
        initListener();
    }





}
