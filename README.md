# Spring Certification: Day 1 Lab

A Java 21 / Spring Boot console app for studying the IoC container, bean lifecycle, post-processors, standard scopes, prototype lookup, and a small custom scope.

## Run

```powershell
mvn test
mvn spring-boot:run
```

The demo prints lifecycle events and scope comparisons, then exits. The context closes at exit, so `@PreDestroy` output is visible. Run `mvn package` to produce a runnable jar under `target/`.

Start with [DAY-1-STUDY-GUIDE.md](DAY-1-STUDY-GUIDE.md) for the two-hour lesson, exercises, and answers. For a beginner-friendly explanation of how every class fits together and what happens from startup through shutdown, read [APPLICATION-WALKTHROUGH.md](APPLICATION-WALKTHROUGH.md).