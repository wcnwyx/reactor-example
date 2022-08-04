package com.wcn.reactor.demo;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.List;

public class DemoSubscription<T> implements Subscription {
    private List<T> list;
    private Subscriber<T> subscriber;

    public DemoSubscription(List<T> list, Subscriber<T> subscriber) {
        this.list = list;
        this.subscriber = subscriber;
    }

    @Override
    public void request(long n) {

    }

    @Override
    public void cancel() {

    }
}
