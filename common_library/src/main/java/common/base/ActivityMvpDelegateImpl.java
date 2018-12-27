package common.base;


import android.os.Bundle;

/**
 * 功能:
 * 作者: 崔兴旺
 * 日期: 2018/3/30
 * 备注:
 */
public class ActivityMvpDelegateImpl<V extends MvpView,P extends MvpPresenter<V>> implements ActivityMvpDelegate<V,P> {

    private ProxyMvpCallback<V,P> proxyMvpCallback;

    public ActivityMvpDelegateImpl(MvpCallback<V,P> mvpCallback) {
        proxyMvpCallback=new ProxyMvpCallback<>(mvpCallback);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        proxyMvpCallback.createPresenter();
        proxyMvpCallback.createView();
        proxyMvpCallback.attachView();
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onReStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestory() {
        proxyMvpCallback.detachView();
    }
}
