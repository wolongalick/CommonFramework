package common.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * 功能:
 * 作者: 崔兴旺
 * 日期: 2018/3/9
 * 备注:
 */
public class RxBus {


    private Subject<Object> bus;
    private static RxBus instance = null;

    private RxBus() {
        bus = PublishSubject.create().toSerialized();
    }

    public static RxBus getInstance() {
        if (instance == null) {
            synchronized (RxBus.class) {
                if (instance == null) {
                    instance = new RxBus();
                }
            }
        }
        return instance;
    }

    /**
     * 之所以用抽象类而不用接口,是因为在获取泛型类型(getGenericSuperclass()方法)时,只能从类上获取,而接口无法获取
     * @param <E>
     */
    public abstract static class OnReceivedListener<E> {
        public abstract void onSubscribe(Disposable disposable);

        public abstract void onReceived(E e);
    }

    /**
     * 订阅事件
     * @param onReceivedListener    接收事件的监听器
     * @param <Event>               事件
     */
    public <Event> void subscibe(final OnReceivedListener<Event> onReceivedListener){
        subscibe(onReceivedListener,false);
    }

    /**
     * 订阅事件
     * @param onReceivedListener    接收事件的监听器
     * @param isDisposable          是否是一次性订阅(接收消息后立即注销监听)
     * @param <Event>               事件
     */
    public <Event> void subscibe(final OnReceivedListener<Event> onReceivedListener, final boolean isDisposable) {
        Type genericSuperclass = onReceivedListener.getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
        Type type = parameterizedType.getActualTypeArguments()[0];
        Class<Event> entityClass = (Class<Event>) type;

        toObservable(entityClass).subscribe(new Observer<Event>() {
            private Disposable disposable;

            @Override
            public void onSubscribe(Disposable disposable) {
                this.disposable=disposable;
                onReceivedListener.onSubscribe(disposable);
            }

            @Override
            public void onNext(Event event) {
                //如果是一次性的事件,则接收事件后立即销毁
                if(isDisposable){
                    if(disposable!=null && !disposable.isDisposed()){
                        disposable.dispose();
                    }
                }
                onReceivedListener.onReceived(event);
            }

            @Override
            public void onError(Throwable e) {
                BLog.e("rxbus报错:" + e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }


    public void post(Object o) {
        bus.onNext(o);
    }

    public boolean hasObservable() {
        return bus.hasObservers();
    }

    /**
     * 转换为特定类型的Obserbale
     */
    private <T> Observable<T> toObservable(Class<T> type) {
        return bus.ofType(type);
    }

    public void dispose(Disposable disposable){
        if(disposable!=null && !disposable.isDisposed()){
            disposable.dispose();
        }
    }
}
