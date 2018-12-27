package common.base;

/**
 * 功能:
 * 作者: 崔兴旺
 * 日期: 2018/3/31
 * 备注:
 */
public class FragmentMvpDelegateImpl<V extends MvpView,P extends MvpPresenter<V>> implements FragmentMvpDelegate<V,P> {
    private ProxyMvpCallback<V,P> proxyMvpCallback;

    public FragmentMvpDelegateImpl(MvpCallback<V,P> mvpCallback) {
        proxyMvpCallback=new ProxyMvpCallback<>(mvpCallback);
    }

    @Override
    public void onCreateView() {
        proxyMvpCallback.createPresenter();
        proxyMvpCallback.createView();
        proxyMvpCallback.attachView();
    }

    @Override
    public void onStart() {

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
