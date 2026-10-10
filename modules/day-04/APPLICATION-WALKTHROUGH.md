# Application Walkthrough: Day 4 AOP Concepts Lab

## The Big Picture

This module is a standalone Spring Boot console application. `ReportService` contains the business operation, while `ReportAuditAspect` advises calls that pass through its Spring proxy. The lab makes the proxy type and each advice outcome visible.

## Follow the Startup

### 1. The application starts

Open `src/main/java/com/garethyoo/springcertification/day4/DayFourApplication.java`. `@SpringBootApplication` starts component scanning from the `day4` package.

### 2. Spring creates the service and proxy

`ReportService` implements `ReportOperations`. Spring Boot's AOP auto-configuration wraps the service in a proxy, using CGLIB by default (`spring.aop.proxy-target-class=true`). Proxy is the general term; CGLIB is the proxy implementation here. Setting that property to `false` requests a JDK dynamic proxy, which implements the service interface. `DayFourDemo` depends on `ReportOperations` and calls the proxy rather than constructing the target itself.

### 3. The aspect selects the operation

`ReportAuditAspect` is annotated with `@Aspect` and `@Component`. Its single `@Pointcut` selects methods on `ReportService`; this expression only connects today's advice to the lab service. The broader pointcut-expression practice is Day 5.

### 4. Advice observes success and failure

`DayFourDemo` first requests a valid report. The aspect records before, around, after-returning, and after events around the target invocation. It then requests a report with a blank name. The service throws, `@AfterThrowing` and `@After` run, and the exception is caught by the demo after it crosses the proxy.

`AdviceEventLog` both prints events and stores them so the tests can verify which advice ran without depending on console capture.

### 5. Compare proxy styles in the tests

Open `src/test/java/com/garethyoo/springcertification/day4/AopConceptsTest.java`. Its nested configurations use `@EnableAspectJAutoProxy` to create a JDK dynamic proxy and a CGLIB proxy from the same service. The assertions use `AopUtils.isJdkDynamicProxy` and `AopUtils.isCglibProxy` to check the concrete proxy types.

## Tests

The tests verify that Spring Boot exposes the advised service through a proxy, successful and failing calls trigger the expected advice categories, exceptions remain visible to callers, and both JDK and CGLIB proxy configurations work.

Run only this module's tests from the repository root with `mvn -pl modules/day-04 test`. Run the console demo with `mvn -pl modules/day-04 spring-boot:run`.