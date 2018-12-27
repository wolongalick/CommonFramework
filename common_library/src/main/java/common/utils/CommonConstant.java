package common.utils;

/**
 * Created by cxw on 2016/6/28.
 */
public class CommonConstant {
    public enum SPKeys {
        MAIN_SHAREDPREF,

        KEY_LOGIN_INFO,

        UUID,

        IMEI,

        ANDROID_ID,
    }

    /**
     * 加载数据时的操作类型
     */
    public static class LoadDataOperate {
        public static final String REFRESH = "REFRESH";                 //下拉刷新
        public static final String LOADMORE = "LOADMORE";               //上拉加载更多
        public static final String PRE_LOADMORE = "PRE_LOADMORE";       //预加载更多
    }


}
