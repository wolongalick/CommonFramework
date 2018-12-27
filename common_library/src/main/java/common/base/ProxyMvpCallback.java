package common.base;

/**
 * 功能: 第二个代理->代理对象->绑定和解绑操作
 * 作者: 崔兴旺
 * 日期: 2018/3/31
 * 备注:
 */
public class ProxyMvpCallback<V extends MvpView, P extends MvpPresenter<V>> implements MvpCallback<V,P>{

    private MvpCallback<V,P> mvpCallback;

    public ProxyMvpCallback(MvpCallback<V, P> mvpCallback) {
        this.mvpCallback = mvpCallback;
    }

    @Override
    public P createPresenter() {
        P presenter=mvpCallback.getMvpPresenter();
        if(presenter==null){
            presenter=mvpCallback.createPresenter();
        }

        //允许Presenter为null
        /*if(presenter==null){
            throw new NullPointerException("presenter不能为空!");
        }*/

        mvpCallback.setMvpPresenter(presenter);
        return presenter;
    }

    @Override
    public P getMvpPresenter() {
        return mvpCallback.getMvpPresenter();
    }

    @Override
    public void setMvpPresenter(P presenter) {
        mvpCallback.setMvpPresenter(presenter);
    }

    @Override
    public V createView() {
        V view=mvpCallback.getMvpView();
        if(view==null){
            view=mvpCallback.createView();
        }

        if(view==null){
            throw new NullPointerException("view不能为空!");
        }

        return view;
    }

    @Override
    public V getMvpView() {
        return mvpCallback.getMvpView();
    }

    public void attachView(){
        P p = getMvpPresenter();
        if(p!=null){
            p.attachView(getMvpView());
        }
    }

    public void detachView(){
        P p = getMvpPresenter();
        if(p!=null){
            p.detachView();
        }
    }

}
