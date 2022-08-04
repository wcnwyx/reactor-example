package com.wcn.reactor.demo;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import java.util.List;

public class DemoPublisher<T> implements Publisher<T> {
    private List<T> list;

    public DemoPublisher(List<T> list) {
        this.list = list;
    }

    @Override
    public void subscribe(Subscriber<? super T> s) {
//        s.onSubscribe(new DemoSubscription<T>(list, s));
    }
}
