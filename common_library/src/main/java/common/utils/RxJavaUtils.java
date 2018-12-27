package common.utils;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import io.reactivex.Emitter;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 创建RxJava的工具类
 * Created by cxw on 2016/11/12.
 */

public class RxJavaUtils {

    private static final String TAG = "RxJavaUtils";

    public interface Callback {
        void onFinish();
    }

    public static <E> Observable<E> createObservable(final RxJavaCallback<E> rxJavaCallback) {
        Observable<E> observable = Observable.create(new ObservableOnSubscribe<E>() {
            // 第一步：初始化Observable
            @Override
            public void subscribe(@NonNull ObservableEmitter<E> observableEmitter) throws Exception {
                rxJavaCallback.subscribe(observableEmitter);
                observableEmitter.onComplete();
            }
        });


        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<E>() {
            // 第三步：订阅
            // 第二步：初始化Observer
            private Disposable mDisposable;

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                mDisposable = d;
            }

            @Override
            public void onNext(@NonNull E e) {
                rxJavaCallback.onNext(e);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e(TAG, "onError : value : " + e.getMessage() + "\n");
                rxJavaCallback.onError(e);
            }

            @Override
            public void onComplete() {
                rxJavaCallback.onComplete();
            }
        });
        return observable;
    }

    /**
     * rxjava的回调类,使用者非必须实现onCompleted方法
     *
     * @param <E>
     */
    public abstract static class RxJavaCallback<E> implements ObservableOnSubscribe<E>, Emitter<E> {

        @Override
        public abstract void onNext(E e);

        /**
         * onCompleted()方法,子类非必须实现
         */
        @Override
        public void onComplete() {

        }

        @Override
        public abstract void onError(Throwable e);
    }

    /**
     * 比RxJavaCallback回调类更加简化(使用者非必须实现onError方法)
     *
     * @param <E>
     */
    public abstract static class SimpleRxJavaCallback<E> extends RxJavaCallback<E> {
        @Override
        public abstract void onNext(E e);

        /**
         * onCompleted()方法,子类非必须实现
         */
        @Override
        public void onComplete() {

        }

        /**
         * onError()方法,子类非必须实现
         *
         * @param e
         */
        @Override
        public void onError(Throwable e) {

        }

    }

    /**
     * 定时任务
     *
     * @param milliseconds
     * @param callback
     */

    public static void timedTask(long milliseconds, final Callback callback) {
        Observable.timer(milliseconds, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        callback.onFinish();
                    }
                });
    }

}
