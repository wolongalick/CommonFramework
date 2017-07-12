package common.base;

import android.app.Activity;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.common.R;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.HashMap;
import java.util.Map;

import common.ui.Topbar;
import common.ui.datacontent.GlobalFrameLayout;
import common.ui.datacontent.IEmptyLayout;
import common.ui.datacontent.IFailLayout;
import common.utils.DeviceUtils;

/**
 * Created by Alick on 2015/10/8.
 */
public abstract class ViewHelperImpl implements IViewHelper {
    //以viewId作为key缓存View
    protected Map<Integer, View> viewMap = new HashMap<>();

    private GlobalFrameLayout globalFrameLayout;

    private SystemBarTintManager tintManager;

    @Override
    public final Topbar getTopbar() {
        return getView(R.id.topbar, Topbar.class);
    }

    /**
     * 显示控件(可以是多个控件ID)
     *
     * @param id
     */
    @Override
    public void setVisiable(int... id) {
        for (int i=0;i<id.length;i++){
            getView(id[i],View.class).setVisibility(View.VISIBLE);
        }
    }

    /**
     * 隐藏控件(可以是多个控件ID)
     *
     * @param id
     */
    @Override
    public void setGone(int... id) {
        for (int i=0;i<id.length;i++){
            getView(id[i],View.class).setVisibility(View.GONE);
        }
    }
    @Override
    public void initGlobalFrameLayout(IFailLayout.OnClickReloadButtonListener onClickReloadButtonListener){
        findGlobalFrameLayout(onClickReloadButtonListener);
    }

    @Override
    public void initGlobalFrameLayout(IFailLayout.OnClickReloadButtonListener onClickReloadButtonListener,IEmptyLayout.OnClickEmptyLayoutListener onClickEmptyLayoutListener){
        findGlobalFrameLayout(onClickReloadButtonListener,onClickEmptyLayoutListener);
    }


    @Override
    public void initGlobalFrameLayout(IFailLayout.OnClickReloadButtonListener onClickReloadButtonListener,View emptyView){
        findGlobalFrameLayout(onClickReloadButtonListener);
        globalFrameLayout.setEmptyView(emptyView);
    }

    @Override
    public void initGlobalFrameLayout(IFailLayout.OnClickReloadButtonListener onClickReloadButtonListener,
        String emptyText){
        findGlobalFrameLayout(onClickReloadButtonListener);
        globalFrameLayout.setEmptyText(emptyText);
    }


    private void findGlobalFrameLayout(IFailLayout.OnClickReloadButtonListener onClickReloadButtonListener) {
        globalFrameLayout=getView(R.id.globalFrameLayout,GlobalFrameLayout.class);
        globalFrameLayout.setOnClickReloadButton(onClickReloadButtonListener);
    }

    private void findGlobalFrameLayout(IFailLayout.OnClickReloadButtonListener onClickReloadButtonListener,IEmptyLayout.OnClickEmptyLayoutListener onClickEmptyLayoutListener) {
        globalFrameLayout=getView(R.id.globalFrameLayout,GlobalFrameLayout.class);
        globalFrameLayout.setOnClickReloadButton(onClickReloadButtonListener);
        globalFrameLayout.setOnClickEmptyLayoutListener(onClickEmptyLayoutListener);
    }

    @Override
    public void showFailView() {
        globalFrameLayout=getView(R.id.globalFrameLayout,GlobalFrameLayout.class);
        globalFrameLayout.showFailView();
    }

    @Override
    public void showLoadingView() {
        globalFrameLayout=getView(R.id.globalFrameLayout,GlobalFrameLayout.class);
        globalFrameLayout.showLoadingView();
    }

    @Override
    public void showEmptyView() {
        globalFrameLayout=getView(R.id.globalFrameLayout,GlobalFrameLayout.class);
        globalFrameLayout.showEmptyView();
    }

    @Override
    public void showRealView() {
        globalFrameLayout=getView(R.id.globalFrameLayout,GlobalFrameLayout.class);
        globalFrameLayout.showRealView();
    }

    @Override
    public void showRealViewWithoutAnimation() {

    }

    @Override
    public void setNavigationBarColor(Activity activity, int colorResId) {
        if (DeviceUtils.checkDeviceHasNavigationBar(activity)){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                // 创建状态栏的管理实例
                if(tintManager==null){
                    tintManager = new SystemBarTintManager(activity);
                }
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                // 激活导航栏设置
                tintManager.setNavigationBarTintEnabled(true);
                tintManager.setNavigationBarTintColor(ContextCompat.getColor(activity, colorResId));
            }
        }
    }

    public void initPtrText(IPullToRefresh iPullToRefresh){
        ILoadingLayout startLabels = iPullToRefresh.getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");
        startLabels.setReleaseLabel("放开刷新...");
        startLabels.setRefreshingLabel("正在刷新...");

        ILoadingLayout endLabels = iPullToRefresh.getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉加载更多...");
        endLabels.setReleaseLabel("放开加载...");
        endLabels.setRefreshingLabel("正在加载...");
    }
}
