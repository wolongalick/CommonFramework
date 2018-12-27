package common.base;

/**
 * 功能: 目标接口->Activity生命周期
 * 作者: 崔兴旺
 * 日期: 2018/3/30
 * 备注:
 */
public interface FragmentMvpDelegate<V extends MvpView,P extends MvpPresenter<V>> {

    void onCreateView();

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onDestory();


}
