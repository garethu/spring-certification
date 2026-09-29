# Day 1: Spring Container and Bean Lifecycle

**Time budget:** 2 hours  
**Goal:** Explain how Spring creates and manages objects, then prove the scope rules in the lab.

## Before You Start

From the project root, run:

```powershell
mvn test
mvn spring-boot:run
```

The app prints a short demonstration and exits. Watch the order of the constructor, post-processor, initialization, and destruction messages.

For a guided explanation of every application class and the complete startup-to-shutdown flow, see [APPLICATION-WALKTHROUGH.md](APPLICATION-WALKTHROUGH.md).

## First Hour: Study

### 1. BeanFactory and ApplicationContext

`BeanFactory` is the basic container contract: it can find and provide beans. `ApplicationContext` extends it and adds application-level facilities such as events, message resolution, resource loading, and integration with Spring infrastructure. A Spring Boot application normally uses an `ApplicationContext`.

In the demo, both interfaces are injected. `DayOneDemo` also prints that the context is a `BeanFactory` and performs a lookup using the smaller `BeanFactory` API.

### 2. Bean creation and lifecycle

For a typical singleton bean using constructor injection, the simplified sequence is:

1. Spring resolves the constructor arguments, creating or retrieving the required dependency beans.
2. Spring calls the bean's constructor with those dependencies. (For setter/field injection, property injection happens after construction.)
3. `BeanPostProcessor.postProcessBeforeInitialization` runs.
4. Initialization callbacks run, including `@PostConstruct`.
5. `BeanPostProcessor.postProcessAfterInitialization` runs. A processor may return a wrapper or proxy.
6. Spring makes the initialized bean available for use.
7. When the context closes, destruction callbacks run, including `@PreDestroy`.

This is a useful model, not every internal lifecycle hook. `Aware` callbacks and `InitializingBean`/custom init methods are additional hooks. `@PostConstruct` cannot run before the constructor because the object must exist first.

`LifecycleProbe` prints its constructor, `@PostConstruct`, and `@PreDestroy` calls. `LabBeanPostProcessor` prints immediately before and after initialization. The numbered `[6. Lifecycle]` runner section points you to those startup/shutdown logs.

### 3. BeanFactoryPostProcessor vs BeanPostProcessor

- A `BeanFactoryPostProcessor` can inspect or change bean definitions before regular beans are instantiated. It works on the factory metadata.
- A `BeanPostProcessor` sees bean instances and can modify or wrap them around initialization.

The custom scope registration is done by a static `@Bean` method returning a `BeanFactoryPostProcessor`. The log appears early during startup. The lifecycle probe's before/after messages come from a `BeanPostProcessor` and appear around its initialization callback.

### 4. Scopes

- `singleton` is the default: one instance per bean definition per container.
- `prototype` creates a new instance each time Spring is asked for that bean. Spring does not manage prototype destruction callbacks after handing the instance to the caller.
- `request` and `session` are web scopes, available when a web-aware context is active. They are not meaningful in this console-only app.
- Custom scopes implement Spring's `Scope` contract and define how instances are stored and when a scope ends. This lab registers a simple thread-bound `lab` conversation scope.

#### What `@Scope` Means in This Lab

There is no Spring `@Prototype` annotation. Both beans use Spring's `@Scope` annotation, but with different scope values:

- `PrototypeProbe` uses `@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)`. The constant's value is Spring's built-in `prototype` scope. Each direct container lookup creates a new object.
- `ConversationProbe` uses `@Scope("lab")`. The string is the name of a custom scope, not a built-in scope and not prototype. `LabConfiguration` registers that name with the bean factory before regular beans are created.

`LabScope` stores objects in a `ThreadLocal` map keyed by bean name. In the current thread, repeated lookups of `ConversationProbe` reuse one object. Calling `endConversation()` runs the registered destruction callbacks and clears the thread's stored objects; a later lookup creates a new object. `ConversationProbe` implements `DisposableBean`, so its `destroy()` method is the callback visible when the conversation ends. This is a small teaching scope, not a production replacement for web request/session scopes.

### The Prototype-in-Singleton Trap

The runner's `[3. Singleton vs prototype lookups]` section performs two container lookups of each type and compares object identity: `CatalogService` is the same object, while each `PrototypeProbe` lookup is a different object. Section `[5. Custom 'lab' scope]` demonstrates a third behavior: reuse within one conversation, followed by a new object after the conversation ends. `PrototypeConsumer` is itself a singleton. Its direct `PrototypeProbe` constructor dependency is resolved once when the consumer is created, so repeated calls on the consumer return the same probe. The dependency's declared scope does not cause Spring to reinject it for each method call.

`ObjectProvider<PrototypeProbe>` is a lazy lookup handle. Calling `getObject()` asks the container for a prototype each time. Other common solutions include `Provider<T>` or a scoped proxy, depending on the desired API and scope.

## Second Hour: Practice

1. Run the app and follow the numbered sections. Write down the lifecycle-message order and identify which output comes from each processor.
2. In section 3, connect the identity comparisons to the default singleton scope and the `@Scope("prototype")` declaration.
3. Find `PrototypeConsumer`. Compare the direct dependency's id with two calls to `nextPrototypeId()`.
4. Compare the `@Scope` declarations on `PrototypeProbe` and `ConversationProbe`. Explain why one is built-in prototype scope and the other is the custom `lab` scope.
5. Find the custom scope registration and `LabScope`. Trace how `get`, `registerDestructionCallback`, and `endConversation` work.
6. Change `PrototypeProbe` to singleton scope and rerun the tests. Which assertion fails, and why?
7. Remove the `ObjectProvider` and add a second direct prototype field. Predict how many instances are created when the singleton starts and whether method calls make more.
8. Optional: add another lifecycle callback or a second `BeanPostProcessor` and observe the output order.

## Self-Check

1. What is the default scope, and what does “singleton” mean in Spring?
2. Can `@PostConstruct` run before a constructor?
3. What does a `BeanFactoryPostProcessor` change, and how is that different from a `BeanPostProcessor`?
4. Why does direct prototype injection into a singleton not create a new dependency on each method call?
5. Name two ways to obtain a fresh prototype from a singleton.
6. Does Spring automatically call destruction callbacks on prototype instances?

### Answers

1. Singleton, meaning one instance per bean definition per Spring container, not one instance globally for the whole JVM.
2. No. The object must be constructed before its initialization callback can run.
3. It works with bean-definition metadata before normal bean creation. A `BeanPostProcessor` works with created bean instances around initialization.
4. Constructor injection is resolved when the singleton is created; Spring does not repeat injection on later method calls.
5. `ObjectProvider<T>.getObject()` and `Provider<T>.get()`; a scoped proxy is another option.
6. No. The caller is responsible for cleanup of prototype instances after Spring returns them.