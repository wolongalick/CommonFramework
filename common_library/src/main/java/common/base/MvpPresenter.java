package common.base;

/**
 * 功能:
 * 作者: 崔兴旺
 * 日期: 2018/2/24
 * 备注:
 */
public interface MvpPresenter<V extends MvpView> {

    /**
     * 绑定view
     * @param view
     */
    void attachView(V view);


    /**
     * 解绑view
     */
    void detachView();

}
