package common.base;


/**
 * Created by Alick on 2015/10/12.
 */
public interface INoData {
    /**
     * 显示loading页
     */
    void showLoadingView();

    /**
     * 显示空页面
     */
    void showEmptyView();
    /**
     * 显示加载失败页面
     */
    void showFailView();

    /**
     * 显示真实内容页面
     */
    void showRealView();

    /**
     * 显示真实内容页面(不使用动画)
     */
    void showRealViewWithoutAnimation();

}
