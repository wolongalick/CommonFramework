package common.listdata.api1;

import android.app.Activity;
import common.base.BasicAdapter;

/**
 * Created by Alick on 2016/9/25.
 */

public interface IActivityListHelper<Model, Adapter extends BasicAdapter<Model>> extends IViewListHelper<Model, Adapter>{
    /**
     * 初始化控件
     * @param activity
     */
    void initWidgets(Activity activity);
}
