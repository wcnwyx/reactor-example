#StepVerifier
***
##Description
***
Until now, your solution for each exercise was checked by passing the `Publisher` 
you defined to a test using a `StepVerifier`.
```markdown
到目前为止，每个练习的解决方案都是通过使用StepVerifier将定义的发布者传递到test来检查的。
```

This class from the `reactor-test` artifact is capable of subscribing to 
any `Publisher` (eg. a `Flux` or an Akka Stream...) and then assert a set 
of user-defined expectations with regard to the sequence.
```markdown
此类来自于reactor-test，能够订阅任何发布者（例如，Flux或Akka Stream），
然后断言一组用户定义的关于序列的期望。
```

If any event is triggered that doesn't match the current expectation, 
the `StepVerifier` will produce an `AssertionError`.
```markdown
如果触发的任何事件与当前预期不匹配，StepVerifier将生成一个AssertionError。
```

You can obtain an instance of `StepVerifier` from the static factory `create`. 
It offers a DSL to set up expectations on the data part and finish with a 
single terminal expectation (completion, error, cancellation...).
```markdown
您可以从静态工厂方法create获取StepVerifier的实例。它提供了一个DSL来设置数据部分的期望，
并以单个终端期望（完成、错误、取消）结束。
```

**Note that you must always call the verify() method** or one of the shortcuts 
that combine the terminal expectation and verify, like `.verifyErrorMessage(String)`. 
Otherwise the `StepVerifier` won't subscribe to your sequence and nothing will 
be asserted.
```markdown
请注意，您必须始终调用verify()方法或verify结合终端期望的快捷方式之一(verifyComplete或verifyError)，
例如.verifyErrorMessage(String)。否则，StepVerifier将不会订阅您的序列，并且不会断言任何内容。
```

```markdown
StepVerifier.create(T<Publisher>)
.{expectations...}.verify()
```

##Practice
***
In these exercises, the methods get a `Flux` or `Mono` as a parameter and you'll 
need to test its behavior. You should create a `StepVerifier` that uses said 
Flux/Mono, describes expectations about it and verifies it.
```markdown
在这些练习中，这些方法将Flux或Mono作为参数，您需要测试其行为。您应该创建一个使用
所述Flux/Mono的StepVerifier，描述对它的期望并进行验证。
```

Let's verify the sequence passed to the first test method emits two specific elements, 
`"foo"` and `"bar"`, and that the `Flux` then completes successfully.
```markdown
让我们验证传递给第一个测试方法的序列是否发出两个特定元素"foo"和"bar"，然后验证通量是否成功完成。

    // Use StepVerifier to check that the flux parameter emits "foo" and "bar" elements then completes successfully.
    void expectFooBarComplete(Flux<String> flux) {
        StepVerifier.create(flux).expectNext("foo", "bar").verifyComplete();
    }
```

Now, let's do the same test but verifying that an exception is propagated at the end.
```markdown
现在，让我们执行相同的测试，但验证是否在最后传播了异常。

    // Use StepVerifier to check that the flux parameter emits "foo" and "bar" elements then a RuntimeException error.
    void expectFooBarError(Flux<String> flux) {
        StepVerifier.create(flux).expectNext("foo", "bar").verifyError(RuntimeException.class);
    }
```

Let's try to create a `StepVerifier` with an expectation on a `User`'s 
`getUsername()` getter. Some expectations can work by checking a `Predicate` 
on the next value, or even by consuming the next value by passing it to 
an assertion library like `Assertions.assertThat(T)` from `AssertJ`. 
Try these lambda-based versions (for instance `StepVerifier#assertNext` 
with a lambda using an AssertJ assertion like `assertThat(...).isEqualTo(...)`):
```markdown
让我们尝试创建一个StepVerifier，期望User.getUsername()。
有些期望可以通过检查下一个值的Predicate谓词来实现，
甚至可以通过将下一个值传递给 类似断言的断言库 比如AssertJ的assertThat(T)。
尝试这些基于lambda的版本（例如StepVerifier#assertNext使用AssertJ断言的lambda，
如assertThat(...).isEqualTo(...)

    // Use StepVerifier to check that the flux parameter emits a User with "swhite"username
    // and another one with "jpinkman" then completes successfully.
    void expectSkylerJesseComplete(Flux<User> flux) {
        StepVerifier.create(flux)
                .expectNextMatches(user -> user.getUsername().equals("swhite"))
                .assertNext(user -> Assertions.assertThat(user.getUsername()).isEqualToIgnoringCase("jpinkman"))
                .verifyComplete();
    }
```

On this next test we will receive a Flux which takes some time to emit. 
As you can expect, the test will take some time to run.
```markdown
在下一个测试中，我们将收到一个需要一些时间才能发射的Flux。正如您所料，测试将需要一些时间才能运行。

    // Expect 10 elements then complete and notice how long the test takes.
    void expect10Elements(Flux<Long> flux) {
        StepVerifier.create(flux).expectNextCount(10).verifyComplete();
    }
```

The next one is even worse: it emits 1 element per second, 
completing only after having emitted 3600 of them!
```markdown
下一个更糟糕：它每秒发射1个元素，只有在发射3600个元素后才能完成！
```

Since we don't want our tests to run for hours, we need a way to speed that 
up while still being able to assert the data itself (eliminating the time factor).
```markdown
由于我们不想让测试运行数小时，我们需要一种方法来加快速度，同时仍然能够断言数据本身（消除时间因素）。
```

Fortunately, `StepVerifier` comes with a **virtual time** option: 
by using `StepVerifier.withVirtualTime(Supplier<Publisher>)`, the verifier 
will temporarily replace default core `Schedulers` (the component that define 
the execution context in Reactor). All these default `Scheduler` are replaced 
by a single instance of a `VirtualTimeScheduler`, which has a virtual clock 
that can be manipulated.
```markdown
幸运的是，StepVerifier附带了一个虚拟时间选项：
通过使用StepVerifier.withVirtualTime(Supplier<Publisher>)，验证器将临时替换默认的
核心调度程序（定义反应堆中执行上下文的组件）。所有这些默认调度程序都被VirtualTimeScheduler
的单个实例取代，该实例具有一个可以操纵的虚拟时钟。
```

In order for the operators to pick up that Scheduler, you should lazily build 
your operator chain inside the lambda passed to withVirtualTime.
```markdown
为了让运算符选择该调度器，您应该在传递给withVirtualTime的lambda内懒惰地构建运算符链。
```

You must then advance time as part of your test scenario, by calling either 
`thenAwait(Duration)` or `expectNoEvent(Duration)`. The former simply advances 
the clock, while the later additionally fails if any unexpected event triggers 
during the provided duration (note that almost all the time there will at least 
be a "subscription" event even though the clock hasn't advanced, so you should 
usually put a `expectSubscription()` after `.withVirtualTime()` if you're going 
to use expectNoEvent right after).
```markdown
然后，您必须将时间推进作为测试场景的一部分，方法是调用thenAwait(Duration)或expectNoEvent(Duration)。
前者只是使时钟提前，而如果在提供的持续时间内触发任何意外事件，则后者会额外失败（请注意，
即使时钟没有提前，几乎所有时间都至少会有一个“订阅”事件，因此如果您要在之后立即使用expectNoEvent，
通常应该在.withVirtualTime()后面放置一个expectSubscription()）。
```

```markdown
StepVerifier.withVirtualTime(() -> Mono.delay(Duration.ofHours(3)))
            .expectSubscription()
            .expectNoEvent(Duration.ofHours(2))
            .thenAwait(Duration.ofHours(1))
            .expectNextCount(1)
            .expectComplete()
            .verify();
```

Let's try that by making a fast test of our hour-long publisher:
```markdown
让我们来尝试这一点，通过快速测试我们长达一小时的发布者

    // Expect 3600 elements at intervals of 1 second, and verify quicker than 3600s
    // by manipulating virtual time thanks to StepVerifier#withVirtualTime, notice how long the test takes
    void expect3600Elements(Supplier<Flux<Long>> supplier) {
        StepVerifier.withVirtualTime(supplier).thenAwait(Duration.ofHours(1)).expectNextCount(3600).verifyComplete();
    }
```