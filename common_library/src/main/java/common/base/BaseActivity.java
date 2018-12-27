package common.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.common.R;
import com.jaeger.library.StatusBarUtil;

import common.permission.BasePermissionActivity;
import common.ui.Topbar;
import common.ui.datacontent.GlobalFrameLayout;
import common.ui.datacontent.IEmptyLayout;
import common.ui.datacontent.IFailLayout;
import common.utils.AppStatusManager;

/**
 * Created by Alick on 2015/10/2.
 */
public abstract class BaseActivity<V extends MvpView,P extends MvpPresenter<V>> extends BasePermissionActivity implements IViewControl, IViewHelper,MvpView,MvpCallback<V,P> {
    private static final java.lang.String TAG = "BaseActivity";

    private ActivityMvpDelegate<V,P> mvpDelegate;
    private P presenter;

    public ActivityMvpDelegate<V, P> getMvpDelegate() {
        if(mvpDelegate==null){
            mvpDelegate=new ActivityMvpDelegateImpl<>(this);
        }
        return mvpDelegate;
    }

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
            return BaseActivity.this.getApplicationContext();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMvpDelegate().onCreate(savedInstanceState);

        /*if(presenter==null){
            presenter=createPresenter();
        }

        if(view==null){
            view= (V) this;
        }

        if(presenter != null){
            presenter.attachView(view);
        }*/

        initParmers(savedInstanceState);
        initViews();

        if(!isNotSetBackground()){
            (((ViewGroup)findViewById(android.R.id.content)).getChildAt(0)).setBackgroundColor(getBackgroundColor());
        }


        /*if (isFitsSystemWindows()) {
            ViewGroup rootView = (ViewGroup) ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
            rootView.setFitsSystemWindows(true);
        }*/
        if (isNeedinitStatusNavigationBar()) {
            initStatusNavigationBar();
        }

        supplementParams(savedInstanceState);

        initValues();


        supplementValues();

    }

    //拓展高德地图初始化需要onCreate周期的bundle
    public void extraOverrideAMapOnCreate(Bundle bundle){

    }

    /**
     * 创建presenter(卡榫函数)
     * @return
     */
    public P createPresenter(){
        return presenter;
    }

    @Override
    public P getMvpPresenter() {
        return presenter;
    }

    @Override
    public void setMvpPresenter(P presenter) {
        this.presenter=presenter;
    }

    @Override
    public V createView() {
        return (V) this;
    }

    @Override
    public V getMvpView() {
        return (V) this;
    }


    /**
     * 补充参数(目的是在调用client端的initValues()方法前,再补充一下)
     */
    protected void supplementParams(Bundle savedInstanceState){
        //空实现,子类可以选择性重写
    }

    /**
     * 补充控件属性值(目的是当子类调用完initValues()方法后,再补充一下)
     */
    public void supplementValues(){

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

    @Override
    protected void onStart() {
        super.onStart();
        getMvpDelegate().onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getMvpDelegate().onReStart();
    }

    protected void onResume() {
        super.onResume();
        getMvpDelegate().onResume();
        AppStatusManager.getInstance().setForeground(true);
    }

    protected void onPause() {
        super.onPause();
        getMvpDelegate().onPause();
        AppStatusManager.getInstance().setForeground(false);
    }

    @Override
    protected void onStop() {
        super.onStop();
        getMvpDelegate().onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseOnDestory();
        getMvpDelegate().onDestory();
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
    public GlobalFrameLayout initGlobalFrameLayout(IFailLayout.OnClickReloadButtonListener onClickReloadButtonListener){
        return iViewHelper.initGlobalFrameLayout(onClickReloadButtonListener);
    }

    @Override
    public GlobalFrameLayout initGlobalFrameLayout(IFailLayout.OnClickReloadButtonListener onClickReloadButtonListener,IEmptyLayout.OnClickEmptyLayoutListener onClickEmptyLayoutListener){
        return iViewHelper.initGlobalFrameLayout(onClickReloadButtonListener,onClickEmptyLayoutListener);
    }

    @Override
    public GlobalFrameLayout initGlobalFrameLayout(IFailLayout.OnClickReloadButtonListener onClickReloadButtonListener,View emptyView){
        return iViewHelper.initGlobalFrameLayout(onClickReloadButtonListener,emptyView);
    }

    @Override
    public GlobalFrameLayout initGlobalFrameLayout(IFailLayout.OnClickReloadButtonListener onClickReloadButtonListener,String emptyText){
        return iViewHelper.initGlobalFrameLayout(onClickReloadButtonListener,emptyText);
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
     * 是否不设置背景颜色
     * @return
     */
    protected boolean isNotSetBackground(){
        return false;
    }
}
