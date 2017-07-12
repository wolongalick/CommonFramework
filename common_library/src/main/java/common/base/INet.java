package common.base;

/**
 * 网络接口
 * Created by cxw on 2015/11/4.
 */
public interface INet {
    /**
     * 当网络请求失败时的回调函数
     * @param reason   错误原因(提示信息)
     */
    void onNetFail(String errorCode, String reason);

    /**
     * 当网络传输结束时的回调函数
     */
    void onNetEnd();


}
