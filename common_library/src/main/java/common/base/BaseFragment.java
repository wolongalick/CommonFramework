package common.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.common.R;

import common.permission.BasePermissionFragment_v4;
import common.ui.Topbar;
import common.ui.datacontent.GlobalFrameLayout;
import common.ui.datacontent.IEmptyLayout;
import common.ui.datacontent.IFailLayout;

/**
 * Created by Alick on 2015/10/2.
 */
public abstract class BaseFragment<V extends MvpView,P extends MvpPresenter<V>> extends BasePermissionFragment_v4 implements IViewControl,IFragmentControl,IViewHelper,MvpView,MvpCallback<V,P>{

    private P presenter;
    private FragmentMvpDelegate<V,P> fragment_MvpDelegate;

    public FragmentMvpDelegate<V, P> getMvpDelegate() {
        if(fragment_MvpDelegate ==null){
            fragment_MvpDelegate =new FragmentMvpDelegateImpl<>(this);
        }
        return fragment_MvpDelegate;
    }

    private IViewHelper iViewHelper =new ViewHelperImpl(){
        @Override
        public <T> T getView(int id, Class<T> clazz) {
            if (viewMap.containsKey(id)) {
                return (T) viewMap.get(id);
            } else {
                View inflaterView = rootView.findViewById(id);
                viewMap.put(id, inflaterView);
                return (T) inflaterView;
            }
        }

        @Override
        public Context getContext() {
            Activity hostActivity = getHostActivity();
            return hostActivity!=null ? hostActivity.getApplicationContext() : null;
        }
    };
    //Fragment布局View
    protected View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        getMvpDelegate().onCreateView();


        initParmers(savedInstanceState);
        this.rootView = inflater.inflate(getLayoutId(), container,false);
        rootView.setBackgroundColor(getBackgroundColor());
        initViews();
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initValues();
    }

    @Override
    public void onStart() {
        super.onStart();
        getMvpDelegate().onStart();
    }

    public void onResume() {
        super.onResume();
        getMvpDelegate().onResume();
//        MobclickAgent.onPageStart(this.getClass().getSimpleName()); //统计页面
    }
    public void onPause() {
        super.onPause();
        getMvpDelegate().onPause();
//        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
    }


    @Override
    public void onStop() {
        super.onStop();
        getMvpDelegate().onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getMvpDelegate().onDestory();
    }

    /**
     * 创建presenter(卡榫函数)
     * @return
     */
    public abstract P createPresenter();

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
        return iViewHelper.getView(id,clazz);
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
    public GlobalFrameLayout initGlobalFrameLayout(IFailLayout.OnClickReloadButtonListener onClickReloadButtonListener, IEmptyLayout.OnClickEmptyLayoutListener onClickEmptyLayoutListener){
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

    /**
     * 获取背景色
     * @return
     */
    public int getBackgroundColor(){
        //默认为通用的全局背景色
        return getContext().getResources().getColor(R.color.common_broundground);
    }

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
}
