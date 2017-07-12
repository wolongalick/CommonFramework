package common.base;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.IPullToRefresh;

import common.ui.Topbar;
import common.ui.datacontent.IEmptyLayout;
import common.ui.datacontent.IFailLayout;

/**
 * 视图的帮助接口
 * Created by Alick on 2015/10/8.
 */
public interface IViewHelper extends INoData{
    /**
     * 获得Topbar
     * @return
     */
    Topbar getTopbar();

    /**
     * 更改控件状态变为显示(可以是多个控件ID)
     * @param id
     */
    void setVisiable(int... id);

    /**
     * 更改控件状态变为隐藏(可以是多个控件ID)
     * @param id
     */
    void setGone(int... id);
    /**
     * 获得视图
     * @param控件 id
     * @param clazz
     * @param <T>
     * @return
     */
    <T> T getView(int id, Class<T> clazz);

    Context getContext();

    /**
     * 初始化空页面
     */
    void initGlobalFrameLayout(IFailLayout.OnClickReloadButtonListener onClickReloadButtonListener);

    void initGlobalFrameLayout(IFailLayout.OnClickReloadButtonListener onClickReloadButtonListener, IEmptyLayout.OnClickEmptyLayoutListener onClickEmptyLayoutListener);

    void initGlobalFrameLayout(IFailLayout.OnClickReloadButtonListener onClickReloadButtonListener,
                               String emptyViewText);

    void initGlobalFrameLayout(IFailLayout.OnClickReloadButtonListener onClickReloadButtonListener,
                               View emptyView);

    void setNavigationBarColor(Activity activity, int colorResId);

    void initPtrText(IPullToRefresh iPullToRefresh);

}
