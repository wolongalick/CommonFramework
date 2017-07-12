package common.listdata.impl;

import android.view.View;

import com.common.R;
import com.socks.autoload.AutoLoadListView;

import common.base.BasicAdapter;
import common.listdata.api1.IFragmentListHelper;
import common.ui.datacontent.SimpleGlobalFrameLayout;

/**
 * Created by Alick on 2016/9/25.
 */

public abstract class FragmentListHelperImpl<Model, Adapter extends BasicAdapter<Model>> extends ViewListHelperImpl<Model, Adapter> implements IFragmentListHelper<Model, Adapter> {
    /**
     * 初始化控件
     * @param rootView
     */
    @Override
    public void initWidgets(View rootView){
        simpleGlobalFrameLayout = (SimpleGlobalFrameLayout) rootView.findViewById(R.id.globalFrameLayout);
        autoLoadListView= (AutoLoadListView) rootView.findViewById(R.id.autoLoadListView);
        initListener();
    }





}
