# Application Walkthrough: Day 2 Dependency Injection Lab

## The Big Picture

This module is a standalone Spring Boot console application. Spring creates an application context, discovers the components under `com.garethyoo.springcertification.day2`, injects their collaborators, and then calls `DayTwoDemo.run()`. The runner prints short results so you can connect the source code to the ideas in the study guide.

## Follow the Startup

### 1. The application starts

Open `src/main/java/com/example/springcertification/day2/DayTwoApplication.java`. Its `main` method calls `SpringApplication.run`. `@SpringBootApplication` enables configuration, auto-configuration, and component scanning beneath the Day 2 package.

### 2. Spring discovers the example beans

`ExampleDependency`, the three injection examples, both message senders, `NotificationService`, and `DayTwoDemo` are components. Spring creates them and resolves each declared dependency.

### 3. Compare injection styles

- `ConstructorInjectionExample` receives `ExampleDependency` in its constructor and stores it in a `final` field.
- `SetterInjectionExample` receives the dependency through a setter marked `@Autowired`.
- `FieldInjectionExample` receives it in a private field marked `@Autowired` after construction.

All three return the same dependency message. Their behavior is similar at runtime, but constructor injection communicates required dependencies most clearly and is easiest to instantiate in a plain unit test.

### 4. Resolve multiple implementations

`EmailMessageSender` and `SmsMessageSender` both implement `MessageSender`. If Spring were asked for a `MessageSender` without more information, it would have two candidates.

`EmailMessageSender` is annotated `@Primary`, so the unqualified constructor parameter in `NotificationService` receives it. The second parameter has `@Qualifier("smsMessageSender")`, selecting the SMS component by its bean name. `NotificationService` exposes one method for each choice.

### 5. Read the demo output

Open `DayTwoDemo.java`. Spring constructor-injects the three examples and `NotificationService` into the runner. It prints one line for each injection style, followed by the selected primary and qualified sender. The runner is only a teaching surface; the tests assert the actual wiring behavior.

## Circular Dependencies

The module intentionally does not register a circular pair of beans. A constructor cycle prevents either bean from being constructed, and Spring Boot rejects circular references by default. In real code, first look for a responsibility that can be extracted or a dependency boundary that can be inverted. A provider or lazy reference can defer resolution when that is genuinely required, but should not be the first response to a cycle.

## Tests

Open `src/test/java/com/example/springcertification/day2/DependencyInjectionTest.java`. The Spring context test verifies all three injection styles and verifies that `@Primary` and `@Qualifier` select the expected sender implementations.

Run only this module's tests from the repository root with `mvn -pl modules/day-02 test`. Run the console demo with `mvn -pl modules/day-02 spring-boot:run`.