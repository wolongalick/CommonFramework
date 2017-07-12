package common.listdata.impl;

import android.app.Activity;

import com.common.R;
import com.socks.autoload.AutoLoadListView;

import common.base.BasicAdapter;
import common.listdata.api1.IActivityListHelper;
import common.ui.datacontent.SimpleGlobalFrameLayout;

/**
 * Created by Alick on 2016/9/25.
 */

public abstract class ActivityListHelperImpl<Model, Adapter extends BasicAdapter<Model>> extends ViewListHelperImpl<Model, Adapter> implements IActivityListHelper<Model, Adapter> {
    /**
     * 初始化控件
     * @param activity
     */
    @Override
    public void initWidgets(Activity activity){
        simpleGlobalFrameLayout = (SimpleGlobalFrameLayout) activity.findViewById(R.id.globalFrameLayout);
        autoLoadListView= (AutoLoadListView) activity.findViewById(R.id.autoLoadListView);
        initListener();
    }

}
