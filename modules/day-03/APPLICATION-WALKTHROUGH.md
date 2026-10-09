# Application Walkthrough: Day 3 Configuration and Components Lab

## The Big Picture

This module is a standalone Spring Boot console application. It demonstrates component scanning, explicit bean registration with `@Bean`, and two ways to extract settings from `application.properties`: `@Value` and `@ConfigurationProperties`.

## Follow the Startup

### 1. The application starts

Open `src/main/java/com/example/springcertification/day3/DayThreeApplication.java`. `@SpringBootApplication` starts component scanning from the `day3` package.

### 2. Spring discovers the service and repository

`CatalogService` is annotated `@Service` and `CatalogRepository` is annotated `@Repository`. Both are component stereotypes, so scanning registers them as beans. Spring resolves the service's constructor dependencies while creating it.

### 3. Java configuration declares a formatter bean

`CatalogConfiguration` is annotated `@Configuration`. Its `catalogLabelFormatter()` method is annotated `@Bean`; the returned `CatalogLabelFormatter` instance is registered in the context. The method name is its default bean name.

### 4. Properties are injected two ways

`CatalogService` uses `@Value("${lab.catalog.name}")` to receive the catalog name as a `String`. In parallel, `CatalogProperties` is annotated with `@ConfigurationProperties(prefix = "lab.catalog")`, and `@ConfigurationPropertiesScan` on the application class registers it. Spring binds `lab.catalog.edition` to the record's `edition` component, and the service receives the record through constructor injection. The demo prints both values with their injection style labeled.

### 5. Read the demo output

`DayThreeDemo` is another scanned `@Component`. It receives the service and formatter, then prints the service's catalog description and the formatted label. The output makes the separate registration approaches visible; the tests verify the beans and bound property value.

## Tests

Open `src/test/java/com/example/springcertification/day3/ConfigurationAndComponentsTest.java`. The tests verify that stereotype beans are available, the Java configuration contributes a formatter, `lab.catalog.name` is injected with `@Value`, and `lab.catalog.edition` binds to `CatalogProperties`.

Run only this module's tests from the repository root with `mvn -pl modules/day-03 test`. Run the console demo with `mvn -pl modules/day-03 spring-boot:run`.