翻译自：[https://tech.io/playgrounds/929/reactive-programming-with-reactor-3/Intro](https://tech.io/playgrounds/929/reactive-programming-with-reactor-3/Intro)

#Introduction to Reactive Programming  
***
Reactor 3 is a library built around the `Reactive Streams` 
specification, bringing the paradigm of **Reactive Programming** 
on the JVM.
```
Reactor 3是一个围绕反应流规范构建的库，在JVM上引入了反应式编程的范例。
```

In this course, you'll familiarize with the Reactor API. 
So let's make a quick introduction to the more general concepts 
in Reactive Streams and Reactive Programming.
```
在本课程中，您将熟悉Reactor API。让我们快速介绍一下反应流和反应编程中更一般的概念。
```
##Why
***
Reactive Programming is a new paradigm in which you use *declarative code* 
(in a manner that is similar to functional programming) in order to build 
asynchronous processing pipelines. It is an event-based model where data 
is pushed to the consumer, as it becomes available: we deal with asynchronous 
sequences of events.
```
反应式编程是一种新的范式，在这种范式中，您使用声明性代码（以类似于函数式编程的方式）来构建异步处理管道。
它是一种基于事件的模型，当数据可用时，数据被推送到消费者手中：我们以异步事件序列来处理。
```

This is important in order to be more efficient with resources and increase 
an application's capacity to serve large number of clients, without the 
headache of writing low-level concurrent or and/or parallelized code.
```markdown
为了更有效地利用资源，提高应用程序为大量客户机服务的能力，而不必编写低级并发或/或并行代码，这一点很重要。
```

By being built around the core pillars of being fully **asynchronous** and **non-blocking**, 
Reactive Programming is an alternative to the more limited ways of doing asynchronous 
code in the JDK: namely *Callback* based APIs and `Future`.
```markdown
通过围绕完全异步和非阻塞的核心支柱构建，反应式编程是JDK中执行异步代码更有限方法的替代方法：
即基于Callback的API和Future。
```

It also facilitates composition, which in turn makes asynchronous code more
readable and maintainable.
```markdown
它还促进了组合，从而使异步代码更具可读性和可维护性。
```

##Reactive Streams
***
The **Reactive Streams** specification is an industry-driven effort to standardize 
Reactive Programming libraries on the JVM, and more importantly specify how they 
must behave so that they are interoperable. Implementors include Reactor 3 but also 
RxJava from version 2 and above, Akka Streams, Vert.x and Ratpack.
```markdown
反应流规范是一项行业驱动的工作，旨在标准化JVM上的反应式编程库，更重要的是指定它们的行为方式，
以使它们具有互操作性。实现者包括Reactor 3，也包括RxJava版本2及更高版本，Akka Streams，Vert.x和Ratpack。
```

It contains 4 very simple interfaces as well as a TCK, which shouldn't be overlooked 
since it is the rules of the specification that bring the most value to it.
```markdown
它包含4个非常简单的接口以及一个TCK，这不应该被忽视，因为规范的规则为它带来了最大的价值。
```

From a user perspective however, it is fairly low-level. Reactor 3 aims at offering an 
higher level API that can be leverage in a large breadth of situations, building it on 
top of Reactive Streams `Publisher`.
```markdown
然而，从用户的角度来看，它是相当低级的。Reactor 3旨在提供一种更高级别的API，该API可以在广泛的情况下使用，
将其构建在反应流Publisher的基础上。
```

##Interactions
***
In reactive stream sequences, the source `Publisher` produces data. But by default, 
it does nothing until a `Subscriber` has registered (**subscribed**), at which point 
it will *push* data to it.
```markdown
在反应流序列中，源Publisher（发布者）生成数据。但默认情况下，在Subscriber（订阅者）注册（订阅）之前，
它什么也不做，订阅发生时它（Publisher）会将数据推送到它（Subscriber）。
```
![Publisher and Subscriber](image/01_PublisherSubscriber.png)

Reactor adds the concept of **operators**, which are chained together to describe 
what processing to apply at each stage to the data. Applying an operator returns a new 
intermediate `Publisher` (in fact it can be thought of as both a Subscriber to the operator 
upstream and a Publisher for downstream). The final form of the data ends up in the final 
`Subscriber` that defines what to do from a user perspective.
```markdown
Reactor增加了运算符（operators）的概念，将运算符链接在一起，以描述在每个阶段对数据应用什么处理。
应用运算符将返回一个新的中间发布者（实际上，它可以被视为运算符上游的订阅者和下游的发布者）。数据的最终形式在
最终订阅者中结束，该订阅者从用户角度定义了要做的事情。
```