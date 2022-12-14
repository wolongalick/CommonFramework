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

    //?????????????????????????????????onCreate?????????bundle
    public void extraOverrideAMapOnCreate(Bundle bundle){

    }

    /**
     * ??????presenter(????????????)
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
     * ????????????(??????????????????client??????initValues()?????????,???????????????)
     */
    protected void supplementParams(Bundle savedInstanceState){
        //?????????,???????????????????????????
    }

    /**
     * ?????????????????????(???????????????????????????initValues()?????????,???????????????)
     */
    public void supplementValues(){

    }

    /**
     * ???????????????
     *
     * @return
     */
    public int getBackgroundColor() {
        //?????????????????????????????????
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
     * ??????Topbar
     *
     * @return
     */
    @Override
    public Topbar getTopbar() {
        return iViewHelper.getTopbar();
    }

    /**
     * ??????????????????????????????(?????????????????????ID)
     *
     * @param id
     */
    @Override
    public void setVisiable(int... id) {
        iViewHelper.setVisiable(id);
    }

    /**
     * ??????????????????????????????(?????????????????????ID)
     *
     * @param id
     */
    @Override
    public void setGone(int... id) {
        iViewHelper.setGone(id);
    }

    /**
     * ????????????
     *
     * @param id
     * @param clazz @return
     * @param?????? id
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
     * ??????FitsSystemWindows,?????????true
     *
     * @return
     */
    protected boolean isFitsSystemWindows() {
        return true;
    }

    /**
     * ??????????????????????????????????????????,?????????true
     *
     * @return
     */
    protected boolean isNeedinitStatusNavigationBar() {
        return true;
    }

    /**
     * ???????????????????????????
     * @return
     */
    protected boolean isNotSetBackground(){
        return false;
    }
}
