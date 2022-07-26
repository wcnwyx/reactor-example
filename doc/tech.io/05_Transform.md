翻译自：[https://tech.io/playgrounds/929/reactive-programming-with-reactor-3/transform](https://tech.io/playgrounds/929/reactive-programming-with-reactor-3/transform)

#Transform
***
##Description
***
Reactor ships with several operators that can be used to transform data.
```markdown
Reactor有几个可用于转换数据的运算符。
```

##Practice
***
In the first place, we will capitalize a `String`. Since this is a simple 1-1 
transformation with no expected latency, we can use the map operator with 
a lambda transforming a T into a U.
```markdown
首先，我们将字符串大写。由于这是一个简单的1-1转换，没有预期的延迟，我们可以使用map运算符和lambda将T转换为U。
```

```markdown
    // Capitalize the user username, firstname and lastname
    Mono<User> capitalizeOne(Mono<User> mono) {
        return mono.map(user -> {
            return new User(user.getUsername().toUpperCase(), user.getFirstname().toUpperCase(), user.getLastname().toUpperCase());
        });
    }
```

We can use exactly the same code on a `Flux`, applying the mapping to each 
element as it becomes available.
```markdown
我们可以在Flux上使用完全相同的代码，在每个元素可用时应用映射。
```

```markdown
    // Capitalize the users username, firstName and lastName
    Flux<User> capitalizeMany(Flux<User> flux) {
        return flux.map(user -> new User(user.getUsername().toUpperCase(), user.getFirstname().toUpperCase(), user.getLastname().toUpperCase()));
    }
```

Now imagine that we have to call a webservice to capitalize our String. 
This new call can have latency so we cannot use the synchronous `map` anymore. 
Instead, we want to represent the asynchronous call as a `Flux` or `Mono`, 
and use a different operator: `flatMap`.
```markdown
现在想象一下，我们必须调用一个webservice来大写字符串。这个新的调用可能有延迟，
因此我们不能再使用同步map。相反，我们希望Flux或Mono进行异步调用，并使用不同的运算符：flatMap。
```

`flatMap` takes a transformation `Function` that returns a `Publisher<U>` 
instead of a `U`. This publisher represents the asynchronous transformation to 
apply to each element. If we were using it with `map`, we'd obtain a stream of 
`Flux<Publisher<U>>`. Not very useful.
```markdown
flatMap采用一个转换函数，该函数返回一个Publisher<U>，而不是一个U。该publisher表示应用于
每个元素的异步转换。如果我们将其与map一起使用，我们将获得一个Flux<Publisher<U>>流。不是很有用。
```

But `flatMap` on the other hand knows how to deal with these inner publishers: 
it will subscribe to them then merge all of them into a single global output, 
a much more useful `Flux<U>`. Note that if values from inner publishers arrive 
at different times, they can interleave in the resulting `Flux`.
```markdown
但另一方面，flatMap知道如何处理这些内部发布者：它将订阅这些发布者，
然后将所有发布者合并为一个全局输出，这是一个更有用的Flux<U>。
请注意，如果来自内部发布者的值在不同的时间到达，它们可以交叉插入到产生的Flux中。
```

```markdown
    // Capitalize the users username, firstName and lastName using #asyncCapitalizeUser
    Flux<User> asyncCapitalizeMany(Flux<User> flux) {
        return flux.flatMap(user -> Flux.just(new User(user.getUsername().toUpperCase(), user.getFirstname().toUpperCase(), user.getLastname().toUpperCase())));
    }
```