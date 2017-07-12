package common.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.common.R;
import com.handmark.pulltorefresh.library.IPullToRefresh;

import common.permission.BasePermissionFragment_v4;
import common.ui.Topbar;
import common.ui.datacontent.IEmptyLayout;
import common.ui.datacontent.IFailLayout;

/**
 * Created by Alick on 2015/10/2.
 */
public abstract class BaseFragment_v4 extends BasePermissionFragment_v4 implements IViewControl,IViewHelper{
    private IRxBusHelper iRxBusHelper=new IRxBusHelperImpl();

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
            return getHostActivity();
        }
    };
    //Fragment布局View
    protected View rootView;
    private LayoutInflater inflater;
    private Activity activity;



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity =  getActivity();
        inflater = LayoutInflater.from(context);
    }

    public Activity getHostActivity() {
        if(activity ==null){
            activity =getActivity();
        }
        return activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        initParmers();
        initViews();
        extraOverrideAMapOnCreate(savedInstanceState);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initValues();
    }

    public void onResume() {
        super.onResume();
//        MobclickAgent.onPageStart(this.getClass().getSimpleName()); //统计页面
    }
    public void onPause() {
        super.onPause();
//        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        iRxBusHelper.unAllSubscription();
        releaseOnDestory();
    }

    //拓展高德地图初始化需要onCreate周期的bundle
    public void extraOverrideAMapOnCreate(Bundle bundle){

    }

    @Override
    public void onDetach() {
        super.onDetach();
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
        return iViewHelper.getView(id,clazz);
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
    public void initGlobalFrameLayout(IFailLayout.OnClickReloadButtonListener onClickReloadButtonListener,
        String emptyText){
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

    //==========BaseFragment自己特有的方法---begin==========

    /**
     * 设置Fragment布局
     *
     * @param layoutId
     * @return
     */
    public final View inject(int layoutId) {
        this.rootView = inflater.inflate(layoutId, null);
        rootView.setBackgroundColor(getBackgroundColor());
        return rootView;
    }

    /**
     * 获取背景色
     * @return
     */
    public int getBackgroundColor(){
        //默认为通用的全局背景色
        return getContext().getResources().getColor(R.color.common_broundground);
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


    //==========BaseFragment自己特有的方法---end==========
}
