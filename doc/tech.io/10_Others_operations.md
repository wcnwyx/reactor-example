翻译自：[https://tech.io/playgrounds/929/reactive-programming-with-reactor-3/OthersOperations](https://tech.io/playgrounds/929/reactive-programming-with-reactor-3/OthersOperations)

#Other Operations
***
##Description
***
In this section, we'll have a look at a few more useful operators that don't 
fall into the broad categories we explored earlier. Reactor 3 contains a lot 
of operators, so don't hesitate to have a look at the 
[Flux](https://projectreactor.io/docs/core/release/api/reactor/core/publisher/Flux.html) 
and [Mono](https://projectreactor.io/docs/core/release/api/reactor/core/publisher/Mono.html) 
javadocs as well as the [reference guide](https://projectreactor.io/docs/core/release/reference/docs/index.html#which-operator)
to learn about more of them.
```markdown
在本节中，我们将了解一些更有用的运算符，这些运算符不属于我们前面探讨的大类别。
Reactor 3包含很多运算符，所以请毫不犹豫地查看Flux和Mono javadocs以及参考指南，以了解更多。
```

##Practice
***
In the first exercise we'll receive 3 `Flux<String>`. Their elements could 
arrive with latency, yet each time the three sequences have all emitted 
an element, we want to combine these 3 elements and create a new `User`. 
This concatenate-and-transform operation is called `zip`:
```markdown
在第一个练习中，我们将收到3个Flux<String>。它们的元素可能会延迟到达，但每次这三个序列
都发出一个元素，我们希望将这三个元素结合起来，创建一个新用户。这种连接和转换操作称为zip：
```

```markdown
    // Create a Flux of user from Flux of username, firstname and lastname.
    Flux<User> userFluxFromStringFlux(Flux<String> usernameFlux, Flux<String> firstnameFlux, Flux<String> lastnameFlux) {
        return Flux.zip(usernameFlux, firstnameFlux, lastnameFlux).map(tuples->new User(tuples.getT1(), tuples.getT2(), tuples.getT3()));
    }
```

If you have 3 possible Mono sources and you only want to keep the one that 
emits its value the fastest, you can use the `firstWithValue` static method:
```markdown
如果您有3个可能的Mono源，并且只想保持发射其值最快的一个，那么可以使用firstWithValue静态方法：
```

```markdown
    // Return the mono which returns its value faster
    Mono<User> useFastestMono(Mono<User> mono1, Mono<User> mono2) {
        return Mono.firstWithValue(mono1, mono2);
    }
```

`Flux` also has the `firstWithValue` static method. Only the first element 
emitted by each `Flux` is considered to select the fastest Flux (which is 
then mirrored in the output):
```markdown
Flux也有firstWithValue静态方法。只考虑每个通量发射的第一个元素来选择最快的通量（然后在输出中镜像）：
```

```markdown
    // Return the flux which returns the first value faster
    Flux<User> useFastestFlux(Flux<User> flux1, Flux<User> flux2) {
        return Flux.firstWithValue(flux1, flux2);
    }
```

Sometimes you're not interested in elements of a `Flux<T>`. If you want to 
still keep a `Flux<T>` type, you can use `ignoreElements()`. But if you really 
just want the completion, represented as a `Mono<Void>`, you can use `then()` instead:
```markdown
有时你对Flux<T>的元素不感兴趣。如果仍要保持Flux<T>类型，可以使用ignoreElements()。
但是，如果您真的只想完成，用Mono<Void>表示，您可以使用then()：
```

```markdown
    // Convert the input Flux<User> to a Mono<Void> that represents the complete signal of the flux
    Mono<Void> fluxCompletion(Flux<User> flux) {
        return flux.then();
    }
```

Reactive Streams does not allow null values in `onNext`. There's an operator 
that allow to *just* emit one value, unless it is null in which case it will 
revert to an *empty* `Mono`. Can you find it?
```markdown
反应流不允许onNext中出现空值。有一个运算符只允许发出一个值，除非它为null，在这种情况下，
它将恢复为空的Mono。你能找到它吗？
```

```markdown
    // Return a valid Mono of user for null input and non null input user (hint: Reactive Streams do not accept null values)
    Mono<User> nullAwareUserToMono(User user) {
        return Mono.justOrEmpty(user);
    }
```

Similarly, if you want to prevent the *empty* `Mono` case by falling back to a 
different one, you can find an operator that does this *switch*:
```markdown
类似地，如果您想通过返回另一个Mono来防止出现空Mono情况，您可以找到一个执行此switch的运算符：
```

```markdown
    // Return the same mono passed as input parameter, expect that it will emit User.SKYLER when empty
    Mono<User> emptyToSkyler(Mono<User> mono) {
        //这两个api都可以实现
        return mono.defaultIfEmpty(User.SKYLER);
        //return mono.switchIfEmpty(Mono.just(User.SKYLER));
    }
```

Sometimes you want to capture all values emitted by `Flux` into separate `List`. 
In this case you can use `collectList` operator that would return `Mono` 
containing that `List`.
```markdown
有时，您希望将Flux发出的所有值捕获到单独的List中。在这种情况下，
您可以使用collectList运算符返回包含该List的Mono。
```

```markdown
    // Convert the input Flux<User> to a Mono<List<User>> containing list of collected flux values
    Mono<List<User>> fluxCollection(Flux<User> flux) {
        return flux.collectList();
    }
```

There are more operators belonging to the *collect* family. You can check them 
out in `Flux` [documentation](https://projectreactor.io/docs/core/release/api/reactor/core/publisher/Flux.html).
```markdown
有更多的运算符属于collect系列。您可以在Flux文档中查看它们。
```
