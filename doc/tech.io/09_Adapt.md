翻译自：[https://tech.io/playgrounds/929/reactive-programming-with-reactor-3/Adapt](https://tech.io/playgrounds/929/reactive-programming-with-reactor-3/Adapt)

#Adapt
***
You can make RxJava3 and Reactor 3 types interact without a single external library.
```markdown
您可以使RxJava3和Reactor 3 类型在没有单个外部库的情况下进行交互。
```

In the first two examples we will adapt from `Flux` to `Flowable`, which implements 
`Publisher`, and vice-versa.
```markdown
在前两个示例中，我们将从Flux调整为Flowable，后者实现了Publisher，反之亦然。
```

This is straightforward as both libraries provide a factory method to do that 
conversion from any `Publisher`. The checker below runs the two opposite conversions 
in one go:
```markdown
这很简单，因为这两个库都提供了一种工厂方法来从任何发布服务器进行转换。
下面的检查器一次性运行两个相反的转换：
```

```markdown
    // TODO Adapt Flux to RxJava Flowable
    Flowable<User> fromFluxToFlowable(Flux<User> flux) {
        return Flowable.fromPublisher(flux);
    }
    
    // TODO Adapt RxJava Flowable to Flux
    Flux<User> fromFlowableToFlux(Flowable<User> flowable) {
        return Flux.from(flowable);
    }
```

The next two examples are a little trickier: we need to adapt between `Flux` and 
`Observable`, but the later doesn't implement `Publisher`.
```markdown
接下来的两个例子有点棘手：我们需要在Flux和Observable之间进行调整，但后者没有实现Publisher。
```

In the first case, you can transform any publisher to `Observable`. In the second 
case, you have to first transform the `Observable` into a `Flowable`, which forces 
you to define a strategy to deal with backpressure (RxJava 3 `Observable` doesn't 
support backpressure).
```markdown
在第一种情况下，您可以将任何发布者转换为Observable。在第二种情况下，您必须首先将Observable对象
转换为Flowable对象，这迫使您定义一种处理背压的策略（RxJava 3 Observable不支持背压）。
```

```markdown
    // Adapt Flux to RxJava Observable
    Observable<User> fromFluxToObservable(Flux<User> flux) {
        return Flowable.fromPublisher(flux).toObservable();
    }
    
    // Adapt RxJava Observable to Flux
    Flux<User> fromObservableToFlux(Observable<User> observable) {
        return Flux.from(Flowable.fromObservable(observable, BackpressureStrategy.DROP));
    }
```

Next, let's try to transform a `Mono` to a RxJava `Single`, and vice-versa. You can 
simply call the `firstOrError` method from `Observable`. For the other way around, 
you'll once again need to transform the `Single` into a `Flowable` first.
```markdown
接下来，让我们尝试将Mono转换为RxJava的Single，反之亦然。您只需从Observable调用firstOrError方法。
另一方面，您将再次需要首先将Single转换为Flowable的。
```

```markdown
    // Adapt Mono to RxJava Single
    Single<User> fromMonoToSingle(Mono<User> mono) {
        return Single.fromPublisher(mono);
    }

    // Adapt RxJava Single to Mono
    Mono<User> fromSingleToMono(Single<User> single) {
        return Mono.from(Flowable.fromSingle(single));
    }
```

Finally, you can easily transform a `Mono` to a Java 8 `CompletableFuture` and vice-versa. 
Notice how these conversion methods all begin with `from` (when converting an external 
type to a Reactor one) and `to` (when converting a Reactor type to an external one).
```markdown
最后，您可以轻松地将Mono转换为Java 8 CompletableFuture，反之亦然。注意，这些转换方法都是从from
（将外部类型转换为Reactor类型时）和to（将Reactor类型转换为外部类型时）开始的。
```

```markdown
    // Adapt Mono to Java 8+ CompletableFuture
    CompletableFuture<User> fromMonoToCompletableFuture(Mono<User> mono) {
        return mono.toFuture();
    }

    // Adapt Java 8+ CompletableFuture to Mono
    Mono<User> fromCompletableFutureToMono(CompletableFuture<User> future) {
        return Mono.fromFuture(future);
    }
```