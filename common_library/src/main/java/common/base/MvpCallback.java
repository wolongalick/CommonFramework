package common.base;

/**
 * 功能:
 * 作者: 崔兴旺
 * 日期: 2018/3/31
 * 备注:
 */
public interface MvpCallback<V extends MvpView, P extends MvpPresenter<V>> {

    P createPresenter();

    P getMvpPresenter();

    void setMvpPresenter(P presenter);

    V createView();

    V getMvpView();



}
