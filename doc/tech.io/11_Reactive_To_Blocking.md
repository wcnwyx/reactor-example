翻译自：[https://tech.io/playgrounds/929/reactive-programming-with-reactor-3/ReactiveToBlocking](https://tech.io/playgrounds/929/reactive-programming-with-reactor-3/ReactiveToBlocking)

#Reactive to Blocking
***
Sometimes you can only migrate part of your code to be reactive, and you need 
to reuse reactive sequences in more imperative code.
```markdown
有时，您只能将部分代码迁移为反应式代码，并且需要在更重要的代码中重用反应式序列。
```

Thus if you need to block until the value from a `Mono` is available, use 
`Mono#block()` method. It will throw an `Exception` if the `onError` event 
is triggered.
```markdown
因此，如果需要阻塞，直到Mono的值可用，请使用Mono#block()方法。如果触发onError事件，它将引发异常。
```

Note that you should avoid this by favoring having reactive code end-to-end, 
as much as possible. You MUST avoid this at all cost in the middle of other 
reactive code, as this has the potential to lock your whole reactive pipeline.
```markdown
注意，您应该尽可能多地使用反应式代码来避免这种情况。您必须在其他反应性代码中不惜一切代价避免这种情况，
因为这有可能锁定您的整个反应性管道。
```

```markdown
    // Return the user contained in that Mono
    User monoToValue(Mono<User> mono) {
        return mono.block();
    }
```

Similarly, you can block for the first or last value in a `Flux` with 
`blockFirst()`/`blockLast()`. You can also transform a `Flux` to an `Iterable` 
with `toIterable`. Same restrictions as above still apply.
```markdown
类似地，可以使用blockFirst()/blockLast()阻塞Flux中的第一个或最后一个值。
您还可以使用 toIterable 将Flux转换为Iterable。上述限制仍然适用。
```

```markdown
    // Return the users contained in that Flux
    Iterable<User> fluxToValues(Flux<User> flux) {
        return flux.toIterable();
    }
```