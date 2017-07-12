package common.base;

/**
 * 视图的流程控制接口
 * Created by Alick on 2015/10/8.
 */
public interface IViewControl {
    /**
     * 初始化变量
     */
    void initParmers();

    /**
     * 初始化页面控件
     */
    void initViews();

    /**
     * 初始化页面控件的值以及设置控件监听
     */
    void initValues();

    /**
     * 在onDestory()手动释放资源
     */
    void releaseOnDestory();



}
