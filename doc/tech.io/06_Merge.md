#Merge
***
Merging sequences is the operation consisting of listening for values 
from several `Publishers` and emitting them in a single `Flux`.
```markdown
合并序列是一种操作，包括监听来自多个发布者的值并在单个Flux中发出它们。
```

On this first exercise we will begin by merging elements of two `Flux` 
as soon as they arrive. The caveat here is that values from `flux1` arrive 
with a delay, so in the resulting `Flux` we start seeing values from `flux2` first.
```markdown
在第一个练习中，我们将从两个Flux的元素一到达就合并开始。这里需要注意的是，
来自flux1的值会延迟到达，因此在得到的Flux中，我们首先看到来自flux2的值。

    // Merge flux1 and flux2 values with interleave
    Flux<User> mergeFluxWithInterleave(Flux<User> flux1, Flux<User> flux2) {
        return Flux.merge(flux1, flux2);
    }
```

But if we want to keep the order of sources, we can use the `concat` operator. 
Concat will wait for `flux1` to complete before it can subscribe to `flux2`, 
ensuring that all the values from `flux1` have been emitted, thus preserving 
an order corresponding to the source.
```markdown
但是如果我们想保持来源的顺序，我们可以使用concat操作符。Concat将等待flux1完成，
然后才能订阅flux2，确保flux1中的所有值都已发出，从而保持与源对应的顺序。

    // Merge flux1 and flux2 values with no interleave (flux1 values and then flux2 values)
    Flux<User> mergeFluxWithNoInterleave(Flux<User> flux1, Flux<User> flux2) {
        return Flux.concat(flux1, flux2);
    }
```

You can use `concat` with several `Publisher`. For example, you can get two `Mono` 
and turn them into a same-order `Flux`:
```markdown
您可以将concat与多个发布发布者一起使用。例如，您可以将两个Mono转换为一个 相同顺序的Flux：

    // Create a Flux containing the value of mono1 then the value of mono2
    Flux<User> createFluxFromMultipleMono(Mono<User> mono1, Mono<User> mono2) {
        return Flux.concat(mono1, mono2);
    }
```