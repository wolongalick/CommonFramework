package common.base;

import android.app.Activity;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.WindowManager;

import com.common.R;
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
    public GlobalFrameLayout initGlobalFrameLayout(IFailLayout.OnClickReloadButtonListener onClickReloadButtonListener){
        return findGlobalFrameLayout(onClickReloadButtonListener);
    }

    @Override
    public GlobalFrameLayout initGlobalFrameLayout(IFailLayout.OnClickReloadButtonListener onClickReloadButtonListener,IEmptyLayout.OnClickEmptyLayoutListener onClickEmptyLayoutListener){
        return findGlobalFrameLayout(onClickReloadButtonListener,onClickEmptyLayoutListener);
    }


    @Override
    public GlobalFrameLayout initGlobalFrameLayout(IFailLayout.OnClickReloadButtonListener onClickReloadButtonListener,View emptyView){
        findGlobalFrameLayout(onClickReloadButtonListener);
        globalFrameLayout.setEmptyView(emptyView);
        return globalFrameLayout;
    }

    @Override
    public GlobalFrameLayout initGlobalFrameLayout(IFailLayout.OnClickReloadButtonListener onClickReloadButtonListener,
        String emptyText){
        findGlobalFrameLayout(onClickReloadButtonListener);
        globalFrameLayout.setEmptyText(emptyText);
        return globalFrameLayout;
    }


    private GlobalFrameLayout findGlobalFrameLayout(IFailLayout.OnClickReloadButtonListener onClickReloadButtonListener) {
        globalFrameLayout=getView(R.id.globalFrameLayout,GlobalFrameLayout.class);
        globalFrameLayout.setOnClickReloadButton(onClickReloadButtonListener);
        return globalFrameLayout;
    }

    private GlobalFrameLayout findGlobalFrameLayout(IFailLayout.OnClickReloadButtonListener onClickReloadButtonListener,IEmptyLayout.OnClickEmptyLayoutListener onClickEmptyLayoutListener) {
        globalFrameLayout=getView(R.id.globalFrameLayout,GlobalFrameLayout.class);
        globalFrameLayout.setOnClickReloadButton(onClickReloadButtonListener);
        globalFrameLayout.setOnClickEmptyLayoutListener(onClickEmptyLayoutListener);
        return globalFrameLayout;
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
        globalFrameLayout=getView(R.id.globalFrameLayout,GlobalFrameLayout.class);
        globalFrameLayout.showRealViewWithoutAnimation();
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

}
