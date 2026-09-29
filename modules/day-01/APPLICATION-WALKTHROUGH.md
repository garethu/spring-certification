# Application Walkthrough: Day 1 Spring Container Lab

This guide explains the whole application in the order you encounter it. It assumes you have studied the basic Spring terms, but explains how those terms connect in this code.

## The Big Picture

The application has two main phases:

1. Spring starts and prepares the application context. It finds bean classes, registers the custom scope, creates the required beans, injects dependencies, and runs lifecycle callbacks.
2. After the context is ready, Spring calls `DayOneDemo.run()`. The runner prints examples of container lookup and bean scopes. When the command-line app finishes, the context closes and managed singleton beans receive their destruction callbacks.

The Java program starts in `SpringCertificationApplication.main()`. `DayOneDemo` is not the entry point; Spring calls it later as part of phase 2.

## Follow the Startup

### 1. Java enters the main method

Open `src/main/java/com/example/springcertification/SpringCertificationApplication.java`.

Java starts at `main`. The call to `SpringApplication.run(...)` asks Spring Boot to start the application using this class as its configuration starting point.

`@SpringBootApplication` combines three important jobs:

- It marks this class as a source of Spring configuration.
- It enables Spring Boot auto-configuration, which sets up suitable framework defaults based on the classpath and application settings.
- It enables component scanning from this package and its child packages. That is how Spring finds the lab classes marked with `@Component` and the configuration class marked with `@Configuration`.

This project uses `spring-boot-starter`, not the web starter, so it runs as a console application rather than starting an HTTP server.

### 2. Spring creates and prepares the application context

`SpringApplication.run()` creates an `ApplicationContext`. Think of the context as Spring's container: it keeps the bean definitions, creates bean instances, connects their dependencies, and manages their lifecycle.

The context also implements the `BeanFactory` interface. `BeanFactory` is the basic API for retrieving beans; `ApplicationContext` adds application features such as events and resource handling. `DayOneDemo` receives both interfaces so section 1 can show that relationship.

Before the app is ready, Spring reads configuration and prepares bean definitions. A bean definition is Spring's description of how to create a bean: its class, scope, dependencies, and other settings.

### 3. Spring registers the custom `lab` scope

Open `src/main/java/com/example/springcertification/lab/LabConfiguration.java`.

`@Configuration` tells Spring this class contains configuration. The `@Bean` method supplies a `BeanFactoryPostProcessor`.

A `BeanFactoryPostProcessor` runs early, while Spring is preparing bean definitions and before normal application beans are created. This timing matters: Spring must know what `lab` means before it tries to create `ConversationProbe`, which is marked `@Scope("lab")`.

The processor does two related things:

1. `beanFactory.registerScope("lab", labScope)` teaches Spring to use this `LabScope` implementation whenever it needs a bean whose scope name is `lab`.
2. `beanFactory.registerSingleton("labScope", labScope)` also makes that same scope manager retrievable as a normal singleton bean. The demo and tests inject it so they can call `endConversation()`.

These registrations are not the same operation. The first registers a scope strategy; the second registers the strategy object itself as a bean.

The log line `[factory-post-processor] Registering custom 'lab' scope` comes from this early setup step. It is not printed by `DayOneDemo`.

### 4. Spring registers bean post-processors

Open `src/main/java/com/example/springcertification/lab/LabBeanPostProcessor.java`.

A `BeanPostProcessor` works with bean instances, rather than changing bean definitions. Spring registers post-processors while preparing the context, then applies them as it creates beans.

This one prints messages only when the bean being processed is a `LifecycleProbe`. It returns the same bean unchanged. Real post-processors can also return a wrapper, such as a proxy.

### 5. Spring creates beans and connects dependencies

Spring finds components by scanning the application package. `@Component` makes a class eligible to be a Spring bean. Unless another scope is specified, its scope is singleton: one instance per bean definition in this application context.

Some beans depend on other beans. With constructor injection, Spring first finds or creates the constructor arguments, then calls the constructor with those objects.

For example:

- `CatalogService` needs an `InventoryService`.
- Spring creates or retrieves `InventoryService`, then passes it to the `CatalogService` constructor.
- The `final` field stores that required dependency. `describeInventory()` later delegates to `InventoryService.description()`.

The printed constructor messages let you see when these objects are constructed. The exact order of unrelated singleton beans is not a general guarantee; dependencies must be ready before the bean that needs them.

### Why the Startup Messages Appear in That Order

The console output is not a strict, guaranteed list of every class starting one after another. Read it as a mixture of required ordering and incidental ordering:

- `InventoryService` appears before `CatalogService` because `CatalogService` needs it in its constructor. Spring must resolve the constructor dependency before calling the `CatalogService` constructor.
- `PrototypeProbe` is created when Spring needs one for `PrototypeConsumer`'s direct constructor dependency. Prototype means “create a new object when requested”; it does not mean “wait until the demo prints section 4.”
- `ConversationProbe` is created when Spring first resolves the `@Scope("lab")` dependency needed by `DayOneDemo`. Registering the `lab` scope earlier only teaches Spring how to create/retrieve that bean; it does not itself create the probe.
- The order among unrelated beans, such as `PrototypeConsumer` and `LifecycleProbe`, can reflect discovery and bean-creation details. Do not treat that order as a rule your application can depend on.

`LabBeanPostProcessor` is also different from an ordinary service. Spring registers bean post-processors early so they can inspect other beans as those beans are created. In this lab, the processor has no constructor log. Its visible messages appear only when it processes `LifecycleProbe`: the “before initialization” message is before `@PostConstruct`, and “after initialization” is after it. Those messages do not mean the processor itself started last.

### 6. The lifecycle probe shows initialization hooks

Open `src/main/java/com/example/springcertification/lab/LifecycleProbe.java`.

For this bean, the important visible sequence is:

1. Spring calls the constructor.
2. The `LabBeanPostProcessor` before-initialization hook prints its message.
3. Spring calls the `@PostConstruct` method.
4. The post-processor after-initialization hook prints its message.
5. Spring makes the initialized bean available for use.

This is a simplified view of the lifecycle. There can be additional hooks between these steps, but it explains the messages in this lab. `@PostConstruct` cannot run before the constructor because the object must exist first.

## What `@Scope` Means in These Classes

There is no Spring `@Prototype` annotation. Both examples use Spring's `@Scope` annotation with different values.

### Built-in prototype: `PrototypeProbe`

Open `src/main/java/com/example/springcertification/lab/PrototypeProbe.java`.

`@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)` selects Spring's built-in `prototype` scope. Spring creates a new `PrototypeProbe` each time the container is asked for one. The generated id lets the demo distinguish instances.

A prototype object injected directly into another bean is still created only when Spring resolves that dependency. Spring does not reinject it every time the receiving bean's method is called.

### Custom scope: `ConversationProbe`

Open `src/main/java/com/example/springcertification/lab/scope/ConversationProbe.java`.

`@Scope("lab")` means “use the scope strategy registered under the name `lab`.” It does not mean prototype. It is a custom scope made for this lesson.

Open `src/main/java/com/example/springcertification/lab/scope/LabScope.java` to see that strategy:

- `get(name, objectFactory)` checks the current thread's map for the bean name.
- If the name is already there, it returns the stored object.
- If it is not there, `objectFactory.getObject()` asks Spring to create the bean, and the scope stores it for later lookups.
- Spring calls `registerDestructionCallback` to give the scope cleanup work for a bean.
- `endConversation()` runs those callbacks and clears the current thread's maps. A later lookup therefore creates a new `ConversationProbe`.

`ConversationProbe` implements `DisposableBean`, so its `destroy()` method is one cleanup callback. In this sample, “conversation” is a teaching term: the implementation treats each thread as a conversation. It is not a production web request or session scope.

## Follow the Demo

Open `src/main/java/com/example/springcertification/lab/DayOneDemo.java`. Spring discovers it as a component. It implements `CommandLineRunner`, which tells Spring Boot to call `run()` after the application context has been refreshed and the application has started.

Spring constructor-injects the context APIs, services, custom-scoped probe, and scope manager into `DayOneDemo`. The runner then prints these sections:

1. **Container APIs:** checks that `ApplicationContext` is also a `BeanFactory`, then looks up `catalogService` by name using `BeanFactory`.
2. **Constructor injection:** calls `CatalogService`, which calls its injected `InventoryService`.
3. **Singleton vs prototype lookups:** asks the context for `CatalogService` twice and compares references with `==`; both lookups return the same singleton. It asks for `PrototypeProbe` twice and compares with `!=`; the lookups return different prototype instances.
4. **Prototype injected into a singleton:** `PrototypeConsumer` is a singleton. Its direct `PrototypeProbe` constructor argument was resolved once when the consumer was created, so repeated calls return that captured instance. Its `ObjectProvider` is a handle for asking the container later; each `getObject()` call asks for another prototype.
5. **Custom `lab` scope:** two lookups in the same thread return the same `ConversationProbe`. `endConversation()` runs cleanup and clears the scope. The next lookup creates a new instance, and the runner ends that second conversation too.
6. **Lifecycle:** points you back to the startup lifecycle logs. The `@PreDestroy` message appears when the application context closes, after `run()` returns.

The boolean text `true` means the expected identity comparison succeeded. For example, “same singleton: true” means the two references point to the same object. “Different instances: true” means they point to different objects.

## What Happens When the App Ends

`CommandLineRunner.run()` returns. Because this is a non-web command-line app, Spring Boot finishes the run and closes the application context. Spring calls destruction callbacks for managed singleton beans, including `LifecycleProbe.destroy()` through `@PreDestroy`.

Prototype beans are different: Spring creates them and hands them to the caller, but does not automatically manage their later destruction. The custom `lab` scope does run its own callbacks when this demo explicitly calls `endConversation()`.

## Tests

Open `src/test/java/com/example/springcertification/lab/ScopeBehaviorTest.java`.

`@SpringBootTest` starts a Spring application context for the tests. This also means the `CommandLineRunner` runs while the test context starts, so you will see the demo output in test logs too.

The three tests check:

- two singleton lookups return the same `CatalogService`;
- two prototype lookups return different `PrototypeProbe` objects, a directly injected prototype stays captured by the singleton consumer, and `ObjectProvider` supplies fresh instances;
- the custom scope reuses its object until `endConversation()` is called, then creates a new one.

Run `mvn test` from the project root to execute them.

## Suggested Reading Order

1. Read `SpringCertificationApplication.java` and locate `main`.
2. Read `LabConfiguration.java`; understand why the `lab` scope must be registered early.
3. Read `LabScope.java` and `ConversationProbe.java`; trace custom-scope lookup and cleanup.
4. Read `InventoryService.java` and `CatalogService.java`; trace constructor injection.
5. Read `PrototypeProbe.java` and `PrototypeConsumer.java`; compare direct injection with `ObjectProvider`.
6. Read `LifecycleProbe.java` and `LabBeanPostProcessor.java`; connect the methods to startup and shutdown logs.
7. Read `DayOneDemo.java` while following its numbered runtime output.
8. Read `ScopeBehaviorTest.java` to see how the same behaviors are asserted automatically.
