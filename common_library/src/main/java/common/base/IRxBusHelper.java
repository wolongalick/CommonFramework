package common.base;

import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by cxw on 2016/10/19.
 */

public interface IRxBusHelper {
    /**
     * 增加订阅
     * @param subscription
     */
    void addSubscription(Subscription subscription);

    /**
     * 增加订阅
     * @param tag
     * @param action1
     * @param <T>
     */
    <T> void addSubscription(Object tag,Action1<T> action1);

    /**
     * 取消全部订阅
     */
    void unAllSubscription();

}
