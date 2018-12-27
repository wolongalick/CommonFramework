package common.listdata.impl2;

import android.view.View;

import com.common.R;

import common.base.adapter.BasicRecyclerAdapter;
import common.base.viewholder.BaseViewHolder;
import common.listdata.api2.IFragmentListHelper2;

/**
 * Created by Alick on 2016/9/25.
 */

public abstract class FragmentListHelperImpl2<Model,Holder extends BaseViewHolder, Adapter extends BasicRecyclerAdapter<Model,Holder>> extends ViewListHelperImpl2<Model,Holder, Adapter> implements IFragmentListHelper2<Model,Holder, Adapter> {
    /**
     * 初始化控件
     * @param rootView
     */
    @Override
    public void initWidgets(View rootView){
        simpleGlobalFrameLayout2 =rootView.findViewById(R.id.globalFrameLayout);
        smartRefreshLayout = simpleGlobalFrameLayout2.findViewById(R.id.refreshLayout);
        recyclerView = simpleGlobalFrameLayout2.findViewById(R.id.recyclerView);
        initListener();
    }





}
