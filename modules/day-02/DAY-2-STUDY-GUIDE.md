# Day 2: Dependency Injection

**Time budget:** 2 hours  
**Goal:** Explain how Spring supplies dependencies, choose an injection style deliberately, and resolve multiple candidates predictably.

## Before You Start

From the repository root, run:

```powershell
mvn -pl modules/day-02 test
mvn -pl modules/day-02 spring-boot:run
```

The Day 2 module runs independently from Day 1. For the class-by-class flow, read [APPLICATION-WALKTHROUGH.md](APPLICATION-WALKTHROUGH.md).

## First Hour: Study

### 1. Dependency Injection

Dependency injection (DI) means a class declares what it needs and an external container supplies those collaborators. In Spring, the application context creates beans and connects their dependencies. This is a form of inversion of control: application classes do not construct and manage every collaborator themselves.

`ExampleDependency` is a component. The three injection examples all use it, but receive it through different mechanisms.

### 2. Constructor Injection

`ConstructorInjectionExample` declares its required dependency as a constructor parameter and stores it in a `final` field. Spring uses the single constructor automatically; `@Autowired` is not required on a class with only one constructor in modern Spring.

Constructor injection is the preferred default for required dependencies. It makes requirements visible, allows immutable fields, and lets ordinary unit tests construct the class directly. A constructor with too many dependencies can also reveal that a class has too many responsibilities.

### 3. Setter Injection

`SetterInjectionExample` uses an `@Autowired` setter. Setter injection is useful when a dependency is genuinely optional or can be changed after construction. Required dependencies injected this way can leave an object temporarily incomplete, so constructor injection is usually clearer for them.

### 4. Field Injection

`FieldInjectionExample` uses `@Autowired` on a private field. Spring injects it reflectively after construction. This is included as a contrast, not a recommendation: the dependency is hidden from the constructor, the field cannot be final, and isolated tests need reflection or a Spring context to supply it.

### 5. `@Autowired`, `@Primary`, and `@Qualifier`

When Spring finds exactly one bean of the requested type, it can inject it by type. When there are multiple `MessageSender` beans, injection by type alone is ambiguous.

- `@Primary` marks `EmailMessageSender` as the default candidate when no further selection is specified.
- `@Qualifier("smsMessageSender")` narrows a dependency to the bean registered with that qualifier/name.
- `@Autowired` asks Spring to resolve and inject the dependency. It appears explicitly on the setter and on the multi-candidate constructor; it is optional on the single-constructor examples.

Prefer a qualifier where a particular implementation is part of the consuming class's intent. Do not use `@Primary` to conceal an unclear design when different consumers need different implementations.

### 6. Circular Dependencies

A circular dependency exists when bean A needs bean B while bean B also needs bean A. Constructor-injected cycles cannot be completed: neither constructor can run until the other bean exists. Spring Boot rejects circular references by default, helping expose the design problem during startup.

First break the cycle by extracting shared responsibilities or introducing a smaller interface/event boundary. `ObjectProvider<T>` or `@Lazy` can defer resolution in a legitimate case, but should not be used to disguise a design that remains unnecessarily cyclic. This module intentionally has an acyclic bean graph; the healthy default is that the application starts without circular references.

## Second Hour: Practice

1. Run the Day 2 app and match each output line to the class that provides the dependency.
2. Compare the constructor, setter, and field examples. For each one, identify when its dependency becomes available and what a plain Java unit test would need to do.
3. Find the two `MessageSender` beans. Remove `@Primary` temporarily and predict which injection point will fail; restore it afterward.
4. Change the qualifier value to a nonexistent bean name. Observe how the startup error explains the unresolved dependency; then restore `smsMessageSender`.
5. Sketch a two-bean constructor cycle. Explain why neither constructor can start and suggest one refactoring that removes the cycle.
6. Add another optional collaborator using setter injection and decide whether `required = false` is actually appropriate for it.

## Self-Check

1. Why is constructor injection a good default for required dependencies?
2. When is setter injection a reasonable choice?
3. Why is field injection harder to test without Spring?
4. What does `@Primary` do when multiple beans match a dependency type?
5. How does `@Qualifier` select a particular implementation?
6. Why does a constructor-injected circular dependency fail, and what should you try before adding lazy lookup?

### Answers

1. It makes required dependencies explicit, supports final fields, and allows direct construction in tests.
2. For a genuinely optional dependency or a dependency that is intentionally reconfigurable after construction.
3. The dependency is hidden and cannot be supplied through the constructor; Spring or reflective field access is needed.
4. It marks one matching bean as the default candidate.
5. It narrows candidate resolution using a qualifier value, commonly the component's bean name.
6. Each constructor needs the other bean to exist first. Refactor responsibilities or introduce a suitable interface/event boundary before using a provider or lazy resolution.