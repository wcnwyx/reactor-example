#Blocking to Reactive
***
##Description
***
The big question is "How to deal with legacy, non reactive code?".
```markdown
最大的问题是“如何处理遗留的、非反应性的代码？”。
```

Say you have blocking code (eg. a JDBC connection to a database), and you 
want to integrate that into your reactive pipelines while avoiding too much 
of an impact on performance.
```markdown
假设您有阻塞代码（例如，到数据库的JDBC连接），您希望将其集成到反应式管道中，同时避免对性能造成太多影响。
```

The best course of action is to isolate such intrinsically blocking parts 
of your code into their own execution context via a `Scheduler`, keeping 
the efficiency of the rest of the pipeline high and only creating extra 
threads when absolutely needed.
```markdown
最好的做法是通过调度器将代码中这些固有的阻塞部分隔离到它们自己的执行上下文中，
从而保持管道其余部分的高效率，并且仅在绝对需要时创建额外的线程。
```

In the JDBC example you could use the `fromIterable` factory method. 
But how do you prevent the call to block the rest of the pipeline?
```markdown
在JDBC示例中，可以使用fromIterable工厂方法。但是，如何防止调用阻塞管道的其余部分呢？
```

##Practice
***
The `subscribeOn` method allow to isolate a sequence from the start on a 
provided `Scheduler`. For example, the `Schedulers.boundedElastic()` will 
create a pool of threads that grows on demand, releasing threads that 
haven't been used in a while automatically. In order to avoid too many 
threads due to abusing of this easy option, the `boundedElastic` Scheduler 
places an upper limit to the number of threads it can create and reuse 
(unlike the now deprecated `elastic()` one).
```markdown
subscribeOn方法允许在提供的Scheduler上从开始隔离序列。例如，Schedulers.boundedElastic()
将创建一个按需增长的线程池，自动释放一段时间未使用的线程。为了避免由于滥用此easy选项而导致过多线程，
boundedElastic调度程序对其可以创建和重用的线程数设定了上限（与现在已弃用的elastic()不同）。
```

Use that trick to slowly read all users from the blocking `repository` 
in the first exercise. Note that you will need to wrap the call to the 
repository inside a `Flux.defer` lambda.
```markdown
在第一个练习中，使用该技巧慢慢读取阻塞repository中的所有用户。
注意，您需要将对repository的调用封装在Flux.defer lambda中。
```
```markdown
    // Create a Flux for reading all users from the blocking repository deferred until the flux is subscribed, and run it with a bounded elastic scheduler
    Flux<User> blockingRepositoryToFlux(BlockingRepository<User> repository) {
        return Flux.defer(() -> Flux.fromIterable(repository.findAll()).subscribeOn(Schedulers.boundedElastic()));
    }
```

For slow subscribers (eg. saving to a database), you can isolate a smaller 
section of the sequence with the `publishOn` operator. Unlike `subscribeOn`, 
it only affects the part of the chain **below** it, switching it to a new `Scheduler`.
```markdown
对于速度较慢的订阅者（例如保存到数据库），可以使用publishOn操作符隔离序列的较小部分。
与subscribeOn不同，它只影响其下方链的一部分，将其切换到新的Scheduler。
```

As an example, you can use `doOnNext` to perform a `save` on the `repository`, 
but first use the trick above to isolate that save into its own execution 
context. You can make it more explicit that you're only interested in knowing 
if the save succeeded or failed by chaining the `then()` operator at the end,
which returns a `Mono<Void>`.
```markdown
例如，您可以使用doOnNext在repository上执行保存，但首先使用上述技巧将该保存隔离到其自己的
执行上下文中。通过在末尾链接then()运算符，您可以更明确地知道保存是成功还是失败，这将返回一个Mono<Void>。
```

```markdown
    // Insert users contained in the Flux parameter in the blocking repository using a bounded elastic scheduler and return a Mono<Void> that signal the end of the operation
    Mono<Void> fluxToBlockingRepository(Flux<User> flux, BlockingRepository<User> repository) {
        return flux.publishOn(Schedulers.boundedElastic()).doOnNext(repository::save).then();
    }
```