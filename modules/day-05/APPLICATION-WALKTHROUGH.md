# Application Walkthrough: Day 5 AOP Pointcut Expressions Lab

## The Big Picture

This standalone Spring Boot console application applies one logging aspect to five deliberately distinct targets. Each pointcut uses a different selector, and each advice records a unique marker in `ActivityLog`.

## Follow the Startup

### 1. The application starts

Open `src/main/java/com/garethyoo/springcertification/day5/DayFiveApplication.java`. `@SpringBootApplication` scans the `day5` package, finding the services, repository, aspect, and demo runner.

### 2. The aspect declares five selectors

Open `ActivityLoggingAspect.java`. Its named pointcuts demonstrate:

- `execution(...)` selecting `OrderService.placeOrder` by method signature.
- `within(...)` selecting methods declared in `InventoryService`.
- `@annotation(...)` selecting the method marked with `@TrackAccess`.
- `execution(...) && args(String)` limiting the runtime argument check to notification methods and selecting calls with a `String` argument.
- `bean(*Repository)` selecting calls through the default bean name `orderRepository` (bean-name matching is case-sensitive).

Each pointcut has a separate `@Before` method that records a marker. This makes pointcut matching visible without mixing the exercise with advice-ordering topics from Day 4.

### 3. The demo invokes matching and non-matching methods

`DayFiveDemo` calls the order method, both inventory methods, an annotated and an unannotated customer method, notification methods with different argument types, and the repository method. Compare the application output with the selector each call should satisfy.

### 4. Tests check the pointcut boundaries

Open `src/test/java/com/garethyoo/springcertification/day5/PointcutExpressionsTest.java`. Each test invokes both a matching and, where useful, a non-matching operation, then asserts the exact marker list. These checks help detect expressions that accidentally select too much or too little.

## Tests

Run only this module's tests from the repository root with `mvn -pl modules/day-05 test`. Run the console demo with `mvn -pl modules/day-05 spring-boot:run`.