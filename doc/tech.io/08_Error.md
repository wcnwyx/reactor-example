翻译自：[https://tech.io/playgrounds/929/reactive-programming-with-reactor-3/Error](https://tech.io/playgrounds/929/reactive-programming-with-reactor-3/Error)

#Error
***
##Description
***
Reactor ships with several operators that can be used to deal with errors: 
propagate errors but also recover from it (eg. by falling back to a different 
sequence or by retrying a new `Subscription`).
```markdown
Reactor有几个可用于处理错误的运算符：传播错误，但也可以从中恢复（例如，返回到不同的序列或重试新订阅）。
```

##Practice
***
In the first example, we will return a `Mono` containing default user Saul 
when an error occurs in the original `Mono`, using the method `onErrorReturn`. 
If you want, you can even limit that fallback to the `IllegalStateException` 
class. Use the `User#SAUL` constant.
```markdown
在第一个示例中，当原始Mono中出现错误时，我们将使用onErrorReturn方法返回一个包含默认user Saul的Mono。
如果需要，甚至可以将回退限制为IllegalStateException类。使用用户#SAUL常数。
```

```markdown
    // Return a Mono<User> containing User.SAUL when an error occurs in the input Mono, else do not change the input Mono.
    Mono<User> betterCallSaulForBogusMono(Mono<User> mono) {
        return mono.onErrorReturn(User.SAUL);
    }
```

Let's try the same thing with `Flux`. In this case, we don't just want a single 
fallback value, but a totally separate sequence (think getting stale data from a 
cache). This can be achieved with `onErrorResume`, which falls back to a `Publisher<T>`.
```markdown
让我们用Flux做同样的尝试。在这种情况下，我们不仅需要单个回退值，还需要一个完全独立的序列
（想想从缓存中获取过时数据）。这可以通过onErrorResume实现，其依赖于Publisher<T>。
```

Emit both User#SAUL and User#JESSE whenever there is an error in the original FLux:
```markdown
当原始Flux中出现错误时，发射User#SAUL和User#JESSE：
```

```markdown
    // Return a Flux<User> containing User.SAUL and User.JESSE when an error occurs in the input Flux, else do not change the input Flux.
    Flux<User> betterCallSaulAndJesseForBogusFlux(Flux<User> flux) {
        return flux.onErrorResume(throwable -> Flux.just(User.SAUL, User.JESSE));
    }
```

Dealing with checked exceptions is a bit more complicated. Whenever some code that 
throws checked exceptions is used in an operator (eg. the transformation function 
of a `map`), you will need to deal with it. The most straightforward way is to make 
a more complex lambda with a `try-catch` block that will transform the checked 
exception into a `RuntimeException` that can be signalled downstream.
```markdown
处理选中的异常要复杂一些。每当在运算符（例如映射的转换函数）中使用引发选中异常的代码时，
您都需要处理它。最直接的方法是使用try-catch块生成更复杂的lambda，该块将检查的异常转换
为可以向下游发送信号的运行时异常。
```

There is a `Exceptions#propagate` utility that will wrap a checked exception into a 
special runtime exception that can be automatically unwrapped by Reactor subscribers 
and `StepVerifier`: this avoids seeing an irrelevant `RuntimeException` in the stacktrace.
```markdown
有一个Exceptions#propagate实用程序，它将选中的异常包装成一个特殊的运行时异常，该异常可以由
Reactor订阅者和StepVerifier自动展开：这可以避免在堆栈跟踪中看到不相关的运行时异常。
```

Try to use that on the `capitalizeMany` method within a `map`: you'll need to catch 
a `GetOutOfHereException`, which is checked, but the corresponding test still expects 
the `GetOutOfHereException` directly.
```markdown
尝试在CapitalMany方法上在一个map内部使用：您需要捕获一个GetOutOfHereException，该异常已被选中，
但相应的测试仍然直接预期GetOutOfHereException。
```

```markdown
    // Implement a method that capitalizes each user of the incoming flux using the
    // #capitalizeUser method and emits an error containing a GetOutOfHereException error
    Flux<User> capitalizeMany(Flux<User> flux) {
        return flux.map(user -> {
            try{
                return capitalizeUser(user);
            }catch (GetOutOfHereException e){
                throw Exceptions.propagate(e);
            }
        });
    }

    User capitalizeUser(User user) throws GetOutOfHereException {
        if (user.equals(User.SAUL)) {
            throw new GetOutOfHereException();
        }
        return new User(user.getUsername(), user.getFirstname(), user.getLastname());
    }
```