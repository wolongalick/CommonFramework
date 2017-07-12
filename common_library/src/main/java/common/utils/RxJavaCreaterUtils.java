package common.utils;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 创建RxJava的工具类
 * Created by cxw on 2016/11/12.
 */

public class RxJavaCreaterUtils {
    /**
     * 创建Observable
     * @param rxJavaCallback
     * @param <E>
     * @return
     */
    public static <E> Observable<E> createObservable(final RxJavaCallback<E> rxJavaCallback){
        Observable<E> observable = Observable.create(new Observable.OnSubscribe<E>() {
            @Override
            public void call(Subscriber<? super E> subscriber) {
                rxJavaCallback.call(subscriber);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        observable.subscribe(new Subscriber<E>() {
            @Override
            public void onCompleted() {
                rxJavaCallback.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                BLog.e(e.getMessage());
                rxJavaCallback.onError(e);
            }

            @Override
            public void onNext(E e) {
                rxJavaCallback.onNext(e);
            }
        });
        return observable;
    }

    /**
     * rxjava的回调类,使用者非必须实现onCompleted方法
     * @param <E>
     */
    public abstract static class RxJavaCallback<E> extends Subscriber<E> implements Observable.OnSubscribe<E>{
        @Override
        public abstract void onNext(E e);

        /**
         * onCompleted()方法,子类非必须实现
         */
        @Override
        public void onCompleted() {

        }

        @Override
        public abstract void onError(Throwable e);
    }

    /**
     * 比RxJavaCallback回调类更加简化(使用者非必须实现onError方法)
     * @param <E>
     */
    public abstract static class SimpleRxJavaCallback<E> extends RxJavaCallback<E> {
        @Override
        public abstract void onNext(E e);

        /**
         * onCompleted()方法,子类非必须实现
         */
        @Override
        public void onCompleted() {

        }

        /**
         * onError()方法,子类非必须实现
         * @param e
         */
        @Override
        public void onError(Throwable e){

        }
    }



}
