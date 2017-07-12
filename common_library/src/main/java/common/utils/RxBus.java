package common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

/**
 * Created by lhj on 2016/8/18.
 */
public class RxBus {


    private final HashMap<Object, List<Subject>> rxMap;

    private RxBus() {
        rxMap = new HashMap<>();
    }

    public static RxBus getInstance() {
        return RxbusHolder.rxBus;
    }

    private static class RxbusHolder {
        private static final RxBus rxBus = new RxBus();
    }

    public void send(Object tag, Object object) {
        List<Subject> subjects = rxMap.get(tag);
        if (subjects != null && !subjects.isEmpty()) {
            for (Subject s : subjects) {
                //noinspection unchecked
                s.onNext(object);
            }
        }
    }

    public void send(Object tag) {
        List<Subject> subjects = rxMap.get(tag);
        if (subjects != null && !subjects.isEmpty()) {
            for (Subject s : subjects) {
                //noinspection unchecked
                s.onNext(tag);
            }
        }
    }

    public <T> Subscription toSubscription(Object tag, Action1<T> action1) {
        List<Subject> rxList = rxMap.get(tag);
        if (rxList == null) {
            rxList = new ArrayList<>();
            rxMap.put(tag, rxList);
        }
        Subject<T, T> subject = PublishSubject.create();
        rxList.add(subject);
        return subject
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1);
    }

    public <T> Observable<T> toObserverable(Object tag) {
        List<Subject> rxList = rxMap.get(tag);
        if (rxList == null) {
            rxList = new ArrayList<>();
            rxMap.put(tag, rxList);
        }
        Subject<T, T> subject = PublishSubject.create();
        rxList.add(subject);
        return subject;
    }

    public void unregister(Object tag, Observable observable) {
        List<Subject> subjects = rxMap.get(tag);
        if (subjects != null) {
            subjects.remove(observable);
            if (subjects.isEmpty()) {
                rxMap.remove(tag);
            }
        }
    }
}
