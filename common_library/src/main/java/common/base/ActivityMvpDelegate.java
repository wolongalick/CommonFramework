package common.base;

import android.os.Bundle;

/**
 * 功能: 目标接口->Activity生命周期
 * 作者: 崔兴旺
 * 日期: 2018/3/30
 * 备注:
 */
public interface ActivityMvpDelegate<V extends MvpView,P extends MvpPresenter<V>> {

    void onCreate(Bundle savedInstanceState);

    void onStart();

    void onReStart();

    void onResume();

    void onPause();

    void onStop();

    void onDestory();
}
