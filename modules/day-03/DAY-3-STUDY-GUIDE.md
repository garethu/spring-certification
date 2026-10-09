# Day 3: Configuration and Components

**Time budget:** 2 hours  
**Goal:** Explain how Spring registers beans through component scanning and Java configuration, and how a property value reaches a bean.

## Before You Start

From the repository root, run:

```powershell
mvn -pl modules/day-03 test
mvn -pl modules/day-03 spring-boot:run
```

The Day 3 module runs independently. For the class-by-class flow, read [APPLICATION-WALKTHROUGH.md](APPLICATION-WALKTHROUGH.md).

## First Hour: Study

### 1. Component Scanning

`@SpringBootApplication` includes component scanning rooted at the package containing the application class. In this module, Spring scans `com.garethyoo.springcertification.day3` and its subpackages. Classes marked with component stereotypes are discovered and registered as beans.

### 2. Stereotypes

- `@Component` is the general-purpose stereotype.
- `@Service` marks a service-layer component. It is also a component stereotype and is discovered by scanning.
- `@Repository` marks a persistence-oriented component. It is also discovered by scanning and can participate in persistence exception translation when the relevant infrastructure is present.

`CatalogService` depends on `CatalogRepository`, and Spring supplies the repository through constructor injection. The annotations communicate each class's role; they do not themselves implement business or persistence behavior.

### 3. `@Configuration` and `@Bean`

`@Configuration` marks a class as a source of bean definitions. A method annotated `@Bean` contributes its returned object to the application context; by default, the bean name is the method name. `CatalogConfiguration` registers `CatalogLabelFormatter` explicitly.

Use component scanning when the class is application code you control and a stereotype is a natural fit. Use `@Bean` when registration should be explicit, especially for third-party classes that cannot be annotated or when construction needs custom setup.

### 4. `@ConfigurationProperties` and Property Binding

This module shows both approaches side by side. `CatalogService` uses `@Value("${lab.catalog.name}")` to inject the catalog name as a `String`. Separately, `CatalogProperties` is a record annotated with `@ConfigurationProperties(prefix = "lab.catalog")`; `@ConfigurationPropertiesScan` on `DayThreeApplication` registers it as a bean, and Spring binds `lab.catalog.edition` to its `edition` component. `CatalogService` receives that typed object through constructor injection and uses `catalogProperties.edition()`.

`@Value` is another option for injecting a small number of individual values, for example `${lab.catalog.name:Default Catalog}` with a default. A typed `@ConfigurationProperties` object is more structured and easier to extend when settings belong together; profiles and broader externalized configuration are covered later in the study plan.

## Second Hour: Practice

1. Run the app and identify which printed objects came from scanning and which came from an `@Bean` method.
2. Change `lab.catalog.name` and `lab.catalog.edition` in `application.properties`, rerun the app, and compare the `@Value` and `@ConfigurationProperties` lines in the output.
3. Temporarily remove `@Repository` from `CatalogRepository`. Observe that component scanning no longer registers it and the dependent service prevents startup; restore the annotation.
4. Change the `@Bean` method name and inspect how the default bean name changes. The type-based injection should still resolve while there is only one bean of that type.
5. Add another formatter bean and predict what happens to type-based injection before running the app. Consider `@Qualifier` or `@Primary` if both beans are intentionally needed.
6. Move the application class to a package that does not contain the lab classes. Explain why the default scan root would no longer find them.

## Self-Check

1. What package does `@SpringBootApplication` scan by default?
2. How does `@Service` differ from `@Component` in bean discovery?
3. When is an explicit `@Bean` method useful?
4. What does a `@Bean` method contribute to the application context?
5. How does `${lab.catalog.name}` get resolved?
6. When might `@ConfigurationProperties` be a better fit than many `@Value` expressions?

### Answers

1. The package of the class annotated with `@SpringBootApplication` and its subpackages.
2. Both are component stereotypes discovered by scanning; `@Service` communicates a service-layer role.
3. When creating/configuring a third-party class or when bean construction should be explicit.
4. Its returned object, registered as a bean; absent an explicit name, the method name is the bean name.
5. Spring resolves the placeholder against the Environment, which includes properties from `application.properties`.
6. When settings form a related group and benefit from being bound to a dedicated, structured object.