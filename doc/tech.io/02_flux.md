#Flux
***
##Description
A `Flux<T>` is a Reactive Streams `Publisher`, augmented with a lot of operators 
that can be used to generate, transform, orchestrate Flux sequences.
```markdown
Flux<T> 是一个反应式流发布者，由许多可用于生成、转换和编排Flux序列的操作者组成。
```

It can emit 0 to n `<T>` elements (`onNext` event) then either completes or errors 
(`onComplete` and `onError` terminal events). If no terminal event is triggered, 
the `Flux` is infinite.
```markdown
它可以发出0到n个<T>元素（onNext事件），然后完成或出错（onComplete和onError终端事件）。
如果没有触发终端事件，则Flux是无限的。
```

- Static factories on Flux allow to create sources, or generate them from 
several callbacks types.
    ```markdown
    Flux上的静态工厂允许创建源，或从几种回调类型生成源。
    ```
- Instance methods, the operators, let you build an asynchronous processing 
pipeline that will produce an asynchronous sequence.
    ```markdown
    实例方法（operators）允许您构建一个异步处理管道，该管道将生成异步序列。
    ```
- Each `Flux#subscribe()` or multicasting operation such as `Flux#publish` and 
`Flux#publishNext` will materialize a dedicated instance of the pipeline and 
trigger the data flow inside it.
    ```markdown
    每个Flux#subscribe()或多播操作（如Flux#publish和Flux#publishNext）将具体化管道的专用实例，
    并触发其中的数据流。
    ```

![02_Flux](image/02_flux.png)

`Flux` in action :  
```markdown
Flux.fromIterable(getSomeLongList())
    .delayElements(Duration.ofMillis(100))
    .doOnNext(serviceA::someObserver)
    .map(d -> d * 2)
    .take(3)
    .onErrorResumeWith(errorHandler::fallback)
    .doAfterTerminate(serviceM::incrementTerminate)
    .subscribe(System.out::println);
```

##Practice
***
In this lesson we'll see different factory methods to create a `Flux`.  
```markdown
在本课中，我们将看到创建Flux的不同工厂方法。
```

Let's try a very simple example: just return an empty flux.  
```markdown
让我们尝试一个非常简单的示例：只返回一个空的flux。  

    // TODO Return an empty Flux
    Flux<String> emptyFlux() {
      return Flux.empty();
    }
```

One can also create a Flux out of readily available data:  
```markdown
还可以从现成的数据中创建Flux：  

	// TODO Return a Flux that contains 2 values "foo" and "bar" without using an array or a collection
	Flux<String> fooBarFluxFromValues() {
		return Flux.just("foo", "bar");
	}
```

This time we will use items of a list to publish on the flux 
with `fromIterable`:
```markdown
这一次，我们将使用列表中的项目在flux上发布（通过fromIterable）：

    // TODO Create a Flux from a List that contains 2 values "foo" and "bar"
    Flux<String> fooBarFluxFromList() {
        List<String> list = new ArrayList<String>();
        list.add("foo");
        list.add("bar");
        return Flux.fromIterable(list);
    }
```

In imperative synchronous code, it's easy to manage exceptions with 
familiar `try`-`catch` blocks, `throw` instructions...
```markdown
在命令式同步代码中，使用熟悉的try-catch块、抛出指令很容易管理异常
```

But in an asynchronous context, we have to do things a bit differently. 
Reactive Streams defines the `onError` signal to deal with exceptions. 
Note that such an event **is terminal: this is the last event the `Flux` will produce.**
```markdown
但在异步环境中，我们必须做一些不同的事情。反应流定义onError信号以处理异常。
注意，这样的事件是终端事件：这是Flux将产生的最后一个事件。
```

`Flux#error` produces a `Flux` that simply emits this signal, terminating immediately:
```markdown
Flux#error产生一个Flux，该Flux仅发出【立即终止】信号：

    // TODO Create a Flux that emits an IllegalStateException
    Flux<String> errorFlux() {
        return Flux.error(new IllegalStateException());
    }
```

To finish with `Flux`, let's try to create a Flux that produces ten elements, 
at a regular pace. In order to do that regular publishing, we can use `interval`. 
But it produces an infinite stream (like ticks of a clock), and we want to 
`take` only 10 elements, so don't forget to precise it.
```markdown
为了完成Flux，让我们试着创建一个Flux，以固定的速度生成十个元素。为了定期发布，我们可以使用interval。
但它会产生一个无限流（就像时钟的滴答声），我们只需要10个元素，所以不要忘记对其进行精确计算。

    // TODO Create a Flux that emits increasing values from 0 to 9 each 100ms
    Flux<Long> counter() {
        return Flux.interval(Duration.ofMillis(100)).take(10);
    }
```