# Day 5: AOP Pointcut Expressions

**Time budget:** 2 hours  
**Goal:** Read and write pointcut expressions that select methods by signature, class, annotation, arguments, or Spring bean name, then apply them in a logging aspect.

## Before You Start

From the repository root, run:

```powershell
mvn -pl modules/day-05 test
mvn -pl modules/day-05 spring-boot:run
```

The Day 5 module runs independently. For the class-by-class flow, read [APPLICATION-WALKTHROUGH.md](APPLICATION-WALKTHROUGH.md). Day 4 covered proxy types and advice behavior; this lab focuses on how pointcuts select join points.

## First Hour: Study

### 1. Join Points and Pointcuts

A join point is an execution point that Spring AOP can advise; with proxy-based Spring AOP, the practical join points are method executions invoked through Spring beans. A pointcut is a predicate that selects which of those method executions should be advised. The logging aspect contains five named pointcuts, each connected to one `@Before` advice method.

### 2. `execution(...)`: Match a Method Signature

`execution(* com.garethyoo.springcertification.day5.OrderService.placeOrder(..))` selects `placeOrder` on `OrderService`, regardless of its return type or argument list. In an execution pattern, `*` matches any return type and `..` allows any number of arguments. Use this when a particular method or group of method signatures owns the behavior.

### 3. `within(...)`: Match a Type or Package

`within(com.garethyoo.springcertification.day5.InventoryService)` selects method executions declared within that class. Every externally invoked method on the class is matched, including both stock operations. Package patterns can broaden the scope; avoid broad patterns unless all matched methods should receive the advice.

### 4. `@annotation(...)`: Match an Annotated Method

`@annotation(com.garethyoo.springcertification.day5.TrackAccess)` selects methods carrying `@TrackAccess`. This is useful when the cross-cutting behavior is an explicit opt-in at the method declaration, independent of the class's name.

### 5. `args(...)`: Match Runtime Argument Types

`args(String)` selects a method invocation whose argument list consists of one `String` argument. It checks the runtime argument types, unlike signature patterns such as `execution(...)`. The lab combines it with an `execution(...)` scope for `NotificationService`, so the runtime argument check does not accidentally match unrelated framework beans. The service contrasts `send(String)` with `sendTo(long)`.

### 6. `bean(...)`: Match a Spring Bean Name

`bean(*Repository)` selects Spring beans whose bean name ends with `Repository`. This is a Spring-specific pointcut designator (not a general AspectJ designator), and matching is case-sensitive. The default bean name for `OrderRepository` is `orderRepository`, so only calls through that bean are logged.

### 7. Combine Pointcuts Deliberately

Pointcuts can be combined with `&&` (both conditions), `||` (either), and `!` (negation). For example, a package scope can be narrowed to one annotation. Parenthesize compound expressions so their intent is obvious. Keep the selector close to its intended scope; a broad selector can create surprising logging or performance overhead.

## Second Hour: Practice

1. Run the application and map each `[POINTCUT]` marker to the method call that triggered it.
2. Compare the two inventory calls: why does one `within(...)` pointcut match both?
3. Add `@TrackAccess` to `publicCatalog()` and verify that the annotation pointcut now selects it; then decide whether the added logging is desirable.
4. Change `args(String)` to `args(Object)` and predict which one-argument methods it now selects before running the tests.
4. Change the `args(String)` part of the notification pointcut to `args(Object)` and predict which notification method still matches before running the tests.
5. Change the repository bean name temporarily (for example, with `@Repository("archiveRepository")`) and observe how `bean(*repository)` responds. Restore the original name afterward.
6. Combine `within(InventoryService)` with a method-name `execution(...)` selector using `&&`, so only one inventory method is logged.
7. Write one additional pointcut for a method in this module, connect it to a distinct log marker, and add a focused assertion in `PointcutExpressionsTest`.

## Self-Check

1. In Spring AOP, what method executions can be advised?
2. What does `execution(* ..OrderService.placeOrder(..))` match?
3. How does `within(...)` differ from `execution(...)`?
4. When would `@annotation(...)` be a useful selector?
5. What does `args(String)` inspect?
6. Which pointcut designator here is specific to Spring?
7. What do `&&`, `||`, and `!` mean in a combined pointcut?

### Answers

1. Method executions invoked through Spring AOP proxies for Spring beans.
2. Calls to `placeOrder` on a matching `OrderService`, with any return type and any argument list.
3. `within(...)` scopes by declaring type or package; `execution(...)` can select method signatures, including names and argument patterns.
4. When methods should explicitly opt in to cross-cutting behavior via an annotation.
5. The runtime argument types and count for the method invocation.
6. `bean(...)`.
7. Logical AND, OR, and NOT, respectively.