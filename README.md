# Spring Certification Study Labs

Each study day is an independent Java 21 / Spring Boot Maven module. The root POM shares the Java and Spring Boot versions and lets Maven build all modules together.

See the [21-day study plan](21-DAY-STUDY-PLAN.md) and its [editable Excalidraw roadmap](docs/21-day-roadmap.excalidraw) for the full course at a glance.

## Modules

- [Day 1: Spring Container and Bean Lifecycle](modules/day-01/DAY-1-STUDY-GUIDE.md)
- [Day 2: Dependency Injection](modules/day-02/DAY-2-STUDY-GUIDE.md)
- [Day 3: Configuration and Components](modules/day-03/DAY-3-STUDY-GUIDE.md)

## Run a Module

```powershell
mvn -pl modules/day-01 test
mvn -pl modules/day-01 spring-boot:run
```

To run Day 2 instead, use `modules/day-02` in those commands. Each module has its own entry point, dependencies, tests, and runnable application.

To run Day 3, use `modules/day-03` in those commands.

Run `mvn test` at the root to test all modules, or `mvn package` to package them all.