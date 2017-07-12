package common.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import com.common.R;
import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.jaeger.library.StatusBarUtil;

import common.permission.BasePermissionFragmentActivity;
import common.ui.Topbar;
import common.ui.datacontent.IEmptyLayout;
import common.ui.datacontent.IFailLayout;
import common.utils.AppStatusManager;


public abstract class BaseFragmentActivity extends BasePermissionFragmentActivity implements IViewControl,IViewHelper{

    private static final java.lang.String TAG = "BaseFragmentActivity";
    private IRxBusHelper iRxBusHelper=new IRxBusHelperImpl();

    private IViewHelper iViewHelper =new ViewHelperImpl(){
        @Override
        public <T> T getView(int id, Class<T> clazz) {
            if (viewMap.containsKey(id)) {
                return (T) viewMap.get(id);
            } else {
                View inflaterView = findViewById(id);
                viewMap.put(id, inflaterView);
                return (T) inflaterView;
            }
        }

        @Override
        public Context getContext() {
            return BaseFragmentActivity.this;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initParmers();
        initStatusNavigationBar();
        initViews();

        initValues();
    }

    public void onResume() {
        super.onResume();
        AppStatusManager.getInstance().setForeground(true);
    }
    public void onPause() {
        super.onPause();
        AppStatusManager.getInstance().setForeground(false);
    }

    @Override
    public Topbar getTopbar() {
        return iViewHelper.getTopbar();
    }

    @Override
    public void setVisiable(int... id) {
        iViewHelper.setVisiable(id);
    }

    @Override
    public void setGone(int... id) {
        iViewHelper.setGone(id);
    }

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
    public void initGlobalFrameLayout(IFailLayout.OnClickReloadButtonListener onClickReloadButtonListener,String emptyText){
        iViewHelper.initGlobalFrameLayout(onClickReloadButtonListener,emptyText);
    }

    @Override
    public void setNavigationBarColor(Activity activity, int colorResId) {
        iViewHelper.setNavigationBarColor(activity, colorResId);
    }

    @Override
    public void initPtrText(IPullToRefresh iPullToRefresh) {
        iViewHelper.initPtrText(iPullToRefresh);
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

    public void initStatusNavigationBar() {
        StatusBarUtil.setTransparentForImageViewInFragment(this, null);
        setNavigationBarColor(this, R.color.black);
    }

    /**
     * 是否FitsSystemWindows,默认为true
     * @return
     */
    protected boolean isFitsSystemWindows(){
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
