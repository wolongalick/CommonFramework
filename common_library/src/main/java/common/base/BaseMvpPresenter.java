package common.base;

/**
 * 功能:
 * 作者: 崔兴旺
 * 日期: 2018/2/24
 * 备注:
 */
public abstract class BaseMvpPresenter<V extends MvpView> implements MvpPresenter<V> {

    private V view;

    /**
     * 绑定view
     *
     * @param view
     */
    @Override
    public void attachView(V view) {
        this.view=view;
    }

    /**
     * 解绑view
     */
    @Override
    public void detachView() {
        this.view=null;
    }

    /**
     * 获取view
     * @return
     */
    public V getView() {
        return view;
    }
}
