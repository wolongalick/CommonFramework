package common.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.common.R;
import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.jaeger.library.StatusBarUtil;

import common.permission.BasePermissionActivity;
import common.ui.Topbar;
import common.ui.datacontent.IEmptyLayout;
import common.ui.datacontent.IFailLayout;
import common.utils.AppStatusManager;

/**
 * Created by Alick on 2015/10/2.
 */
public abstract class BaseActivity extends BasePermissionActivity implements IViewControl, IViewHelper {
    private static final java.lang.String TAG = "BaseActivity";
    private IRxBusHelper iRxBusHelper=new IRxBusHelperImpl();
    private IViewHelper iViewHelper = new ViewHelperImpl() {
        @Override
        public <T> T getView(int id, Class<T> clazz) {
            if (viewMap.containsKey(id)) {
                return (T) viewMap.get(id);
            } else {
                View view = findViewById(id);
                viewMap.put(id, view);
                return (T) view;
            }
        }

        @Override
        public Context getContext() {
            return BaseActivity.this;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        PushAgent.getInstance(this).onAppStart();

        initParmers();
        initViews();

        /*if (isFitsSystemWindows()) {
            ViewGroup rootView = (ViewGroup) ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
            rootView.setFitsSystemWindows(true);
        }*/
        if (isNeedinitStatusNavigationBar()) {
            initStatusNavigationBar();
        }

        supplementParams(savedInstanceState);

        initValues();
    }

    //拓展高德地图初始化需要onCreate周期的bundle
    public void extraOverrideAMapOnCreate(Bundle bundle){

    }

    /**
     * 补充参数(目的是在调用client端的initValues()方法前,再补充一下)
     */
    protected void supplementParams(Bundle savedInstanceState){
        //空实现,子类可以选择性重写
    }

    /**
     * 获取背景色
     *
     * @return
     */
    public int getBackgroundColor() {
        //默认为通用的全局背景色
        return getContext().getResources().getColor(R.color.common_broundground);
    }


    protected void onResume() {
        super.onResume();
        AppStatusManager.getInstance().setForeground(true);
    }

    protected void onPause() {
        super.onPause();
        AppStatusManager.getInstance().setForeground(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseOnDestory();
    }

    /**
     * 获得Topbar
     *
     * @return
     */
    @Override
    public Topbar getTopbar() {
        return iViewHelper.getTopbar();
    }

    /**
     * 更改控件状态变为显示(可以是多个控件ID)
     *
     * @param id
     */
    @Override
    public void setVisiable(int... id) {
        iViewHelper.setVisiable(id);
    }

    /**
     * 更改控件状态变为隐藏(可以是多个控件ID)
     *
     * @param id
     */
    @Override
    public void setGone(int... id) {
        iViewHelper.setGone(id);
    }

    /**
     * 获得视图
     *
     * @param id
     * @param clazz @return
     * @param控件 id
     */
    @Override
    public <T> T getView(int id, Class<T> clazz) {
        return iViewHelper.getView(id, clazz);
    }

    @Override
    public Context getContext() {
        return iViewHelper.getContext();
    }

    @Override
    public void initGlobalFrameLayout(IFailLayout.OnClickReloadButtonListener onClickReloadButtonListener){
        iViewHelper.initGlobalFrameLayout(onClickReloadButtonListener);
    }

    @Override
    public void initGlobalFrameLayout(IFailLayout.OnClickReloadButtonListener onClickReloadButtonListener,IEmptyLayout.OnClickEmptyLayoutListener onClickEmptyLayoutListener){
        iViewHelper.initGlobalFrameLayout(onClickReloadButtonListener,onClickEmptyLayoutListener);
    }

    @Override
    public void initGlobalFrameLayout(IFailLayout.OnClickReloadButtonListener onClickReloadButtonListener,View emptyView){
        iViewHelper.initGlobalFrameLayout(onClickReloadButtonListener,emptyView);
    }

    @Override
    public void initGlobalFrameLayout(IFailLayout.OnClickReloadButtonListener onClickReloadButtonListener,String emptyText){
        iViewHelper.initGlobalFrameLayout(onClickReloadButtonListener,emptyText);
    }

    @Override
    public void showLoadingView() {
        iViewHelper.showLoadingView();
    }

    @Override
    public void showEmptyView() {
        iViewHelper.showEmptyView();
    }

    @Override
    public void showFailView() {
        iViewHelper.showFailView();
    }

    @Override
    public void showRealView() {
        iViewHelper.showRealView();
    }

    @Override
    public void showRealViewWithoutAnimation() {
        iViewHelper.showRealViewWithoutAnimation();
    }

    @Override
    public void setNavigationBarColor(Activity activity, int colorResId) {
        iViewHelper.setNavigationBarColor(activity, colorResId);
    }

    @Override
    public void initPtrText(IPullToRefresh iPullToRefresh) {
        iViewHelper.initPtrText(iPullToRefresh);
    }

    protected void initStatusNavigationBar() {
        StatusBarUtil.setTransparentForImageView(this,null);
        setNavigationBarColor(this, R.color.black);
    }

    /**
     * 是否FitsSystemWindows,默认为true
     *
     * @return
     */
    protected boolean isFitsSystemWindows() {
        return true;
    }

    /**
     * 是否需要初始化状态栏和导航栏,默认为true
     *
     * @return
     */
    protected boolean isNeedinitStatusNavigationBar() {
        return true;
    }



    /**
     * 增加订阅
     *
     * @param subscription
     */
    /*@Override
    public void addSubscription(Subscription subscription) {
        iRxBusHelper.addSubscription(subscription);
    }*/

    /**
     * 增加订阅
     * @param tag
     * @param action1
     */
    /*@Override
    public void addSubscription(Object tag, Action1 action1) {
        iRxBusHelper.addSubscription(tag,action1);
    }*/

    /**
     * 取消全部订阅
     */
    /*@Override
    public void unAllSubscription() {
        iRxBusHelper.unAllSubscription();
    }*/
}
