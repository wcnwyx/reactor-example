#Mono
***

##Description
***

A `Mono<T>` is a Reactive Streams `Publisher`, also augmented with a lot of operators 
that can be used to generate, transform, orchestrate Mono sequences.
```markdown
Mono是一种反应流发布者，还增加了许多操作者，可用于生成、转换和编排Mono序列。
```

It is a specialization of `Flux` that can emit **at most 1 <T> element**: 
a Mono is either valued (complete with element), empty (complete without element) 
or failed (error).
```markdown
它是Flux的特例，最多可以发射1个元素：Mono要么有值（带元素完成），要么为空（无元素完成），要么为失败（错误）。
```

A `Mono<Void>` can be used in cases where only the completion signal is interesting 
(the Reactive Streams equivalent of a `Runnable` task completing).
```markdown
在只对完成信号感兴趣的情况下（相当于Runnable任务完成的反应流），可以使用Mono<Void>。
```

Like for `Flux`, the operators can be used to define an asynchronous pipeline 
which will be materialized anew for each `Subscription`.
```markdown
与Flux一样，可以使用操作者定义异步管道，该管道将针对每个订阅重新具体化。
```

Note that some API that change the sequence's cardinality will return a `Flux`
(and vice-versa, APIs that reduce the cardinality to 1 in a `Flux` return a `Mono`).
```markdown
请注意，一些更改序列基数的API将返回Flux（反之亦然，在Flux中将基数减少到1的API将返回Mono）。
```

![03_mono](image/03_Mono.png)

`Mono` in action:  
```markdown
Mono.firstWithValue(
    Mono.just(1).map(integer -> "foo" + integer),
Mono.delay(Duration.ofMillis(100)).thenReturn("bar")
    )
    .subscribe(System.out::println);
```

##Practice
***
As for the Flux let's return a empty `Mono` using the static factory.
```markdown
就像Flux，让我们使用静态工厂返回一个空Mono。

    // Return an empty Mono
    Mono<String> emptyMono() {
        return Mono.empty();
    }
```

Now, we will try to create a `Mono` which never emits anything. 
Unlike `empty()`, it won't even emit an `onComplete` event.
```markdown
现在，我们将尝试创建一个从不发射任何东西的Mono。与empty()不同，它甚至不会发出onComplete事件。

    // Return a Mono that never emits any signal
    Mono<String> monoWithNoSignal() {
        return Mono.never();
    }
```

Like `Flux`, you can create a `Mono` from an available (unique) value.
```markdown
与Flux一样，您可以从可用（唯一）值创建Mono。

    // Return a Mono that contains a "foo" value
    Mono<String> fooMono() {
        return Mono.just("foo");
    }
```

And exactly as we did for the flux, we can propagate exceptions.
```markdown
正如我们对Flux所做的那样，我们可以传播异常。

    // Create a Mono that emits an IllegalStateException
    Mono<String> errorMono() {
        return Mono.error(new IllegalStateException());
    }
```