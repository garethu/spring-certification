# Day 4: AOP Concepts

**Time budget:** 2 hours  
**Goal:** Explain why Spring creates AOP proxies, distinguish JDK dynamic proxies from CGLIB proxies, and identify what each common advice type does.

## Before You Start

From the repository root, run:

```powershell
mvn -pl modules/day-04 test
mvn -pl modules/day-04 spring-boot:run
```

The Day 4 module runs independently. For the class-by-class flow, read [APPLICATION-WALKTHROUGH.md](APPLICATION-WALKTHROUGH.md).

## First Hour: Study

### 1. What AOP Adds

Aspect-oriented programming separates cross-cutting behavior, such as auditing, from the core service method. Spring AOP applies that behavior when a call passes through a proxy around a Spring bean. The target service remains responsible for generating a report; `ReportAuditAspect` records what happens around that call.

The proxy boundary matters: a call made from outside the bean can be intercepted, while a method calling another method on `this` bypasses the proxy. Spring AOP is proxy-based, not full bytecode weaving.

**Terminology:** A proxy is the general wrapper Spring uses to intercept calls. JDK dynamic proxies and CGLIB are two ways to create that proxy; they are not alternatives to using a proxy.

### 2. JDK Dynamic Proxies

A JDK dynamic proxy implements one or more interfaces exposed by the target. It can be injected and used through those interfaces, but it is not an instance of the concrete target class. `ReportOperations` is the interface for the report service, and the test creates a JDK proxy explicitly with `@EnableAspectJAutoProxy(proxyTargetClass = false)`.

### 3. CGLIB Proxies

CGLIB creates a subclass of the target class, so callers can use the concrete class type. A subclass cannot override `final` classes or methods, so those cannot be advised this way. Spring Boot's AOP auto-configuration uses CGLIB proxies by default because `spring.aop.proxy-target-class` defaults to `true`. Set it to `false` to request JDK dynamic proxies when the target exposes an interface. The lab test also creates both styles explicitly with `@EnableAspectJAutoProxy`.

Prefer injecting the narrow interface when one exists. Proxy selection can depend on configuration and available interfaces, so use `AopUtils` in the lab rather than inferring the proxy type from the injected declaration alone.

### 4. Advice Types

The aspect applies five kinds of advice to the report operation:

- `@Before` runs before the target method.
- `@AfterReturning` runs only after a normal return.
- `@AfterThrowing` runs only when the target throws.
- `@After` runs after either a return or an exception, similar to a `finally` block.
- `@Around` wraps the invocation. It controls whether and when the target proceeds and must return the target result (or throw an exception).

Advice ordering can affect behavior. The lab focuses on each advice's semantics and does not rely on a particular order among the separate advice methods.

### 5. Where Day 4 Stops

The aspect uses one narrow `execution(...)` expression to select its service method. Today, focus on proxy creation and advice behavior. Day 5 is the separate pointcut-expression exercise: writing several expressions and applying a logging aspect. Do not expand today's single selector into that exercise.

## Second Hour: Practice

1. Run the app. Follow the advice events around the successful report call and identify when the target result appears.
2. Follow the failing call. Which advice runs, and does the original `IllegalArgumentException` still reach the caller?
3. In `AopConceptsTest`, compare the JDK proxy and CGLIB proxy assertions. What type can each proxy be assigned to?
4. Temporarily change the JDK test configuration to `proxyTargetClass = true`. Observe the proxy type, then restore the setting.
5. Add `final` to `ReportService.generate`. Predict which proxy style can still intercept it, run the test, and then restore the method.
6. Find a method on `ReportService` that calls another method on the same instance. Explain why the internal call would bypass this proxy-based advice.

## Self-Check

1. What does a Spring AOP proxy intercept?
2. What does a JDK dynamic proxy implement, and what does that mean for injection types?
3. How does a CGLIB proxy differ, and what kinds of target methods cannot be overridden?
4. Which advice runs only on normal return? Which runs only on an exception?
5. What responsibility does `ProceedingJoinPoint.proceed()` have in around advice?
6. Why does self-invocation normally bypass Spring AOP advice?

### Answers

1. Calls that cross the proxy boundary to an advised Spring bean method.
2. It implements interfaces; callers should use one of those interfaces rather than expect the proxy to be the concrete target class.
3. CGLIB subclasses the target class; final classes and final methods cannot be overridden for interception.
4. `@AfterReturning` runs only on normal return; `@AfterThrowing` runs only when the target throws.
5. It invokes the target method. Around advice must decide whether to call it and should preserve the result or exception as appropriate.
6. The internal call uses `this` on the target object and does not pass through the proxy.