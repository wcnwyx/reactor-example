package com.wcn.reactor.flux;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class FluxTest {
    public static void main(String[] args) {
        Flux<String> flux = Flux.just("a", "b", "c");
        System.out.println("flux:"+flux);
//        flux.subscribe(s -> {
//            System.out.println("thread:"+Thread.currentThread()+" value:"+s);
//        });
//        flux.subscribe(s -> {
//            System.out.println("thread:"+Thread.currentThread()+" value1:"+s);
//        });

        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onSubscribe(Subscription s) {
                System.out.println("onSubscribe:"+s);
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(String s) {
                System.out.println("onNext:"+s);
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("onError:"+t);
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        };

        flux.subscribe(subscriber);
        flux.subscribe(subscriber);
    }
}
