package common.base;

import common.utils.RxBus;
import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by cxw on 2016/10/19.
 */
public class IRxBusHelperImpl implements IRxBusHelper {

    private CompositeSubscription compositeSubscription;

    /**
     * 增加订阅
     * @param subscription
     */
    @Override
    public void addSubscription(Subscription subscription) {
        if (this.compositeSubscription == null) {
            this.compositeSubscription = new CompositeSubscription();
        }
        this.compositeSubscription.add(subscription);
    }

    /**
     * 增加订阅
     * @param tag
     * @param action1
     * @param <T>
     */
    @Override
    public <T> void addSubscription(Object tag, Action1<T> action1) {
        addSubscription(RxBus.getInstance().toSubscription(tag,action1));
    }

    /**
     * 取消全部订阅
     */
    @Override
    public void unAllSubscription() {
        if (this.compositeSubscription != null && compositeSubscription.hasSubscriptions()) {
            this.compositeSubscription.unsubscribe();//取消注册，以避免内存泄露
        }
    }



}
