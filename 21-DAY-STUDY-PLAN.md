# 21-Day Spring Certification Study Plan

**Daily time budget:** 2 hours (approximately 1 hour of theory and 1 hour of hands-on practice in one running application)

## Week 1: Container, AOP, and Testing

### Day 1: Container Basics

ApplicationContext, bean lifecycle, `@PostConstruct`/`@PreDestroy`, and scopes (especially the prototype-in-singleton trap).

### Day 2: Dependency Injection

Constructor vs. setter vs. field injection, `@Autowired`, `@Qualifier`, `@Primary`, and circular dependency handling.

### Day 3: Configuration and Components

`@Configuration`/`@Bean`, component scanning, stereotypes (`@Service`, `@Repository`), `@Value`, and property injection.

### Day 4: AOP Concepts

Proxies (JDK dynamic vs. CGLIB) and advice types (`@Before`, `@After`, `@Around`, etc.).

### Day 5: AOP Pointcut Expressions

Write 4-5 different pointcuts and apply a logging aspect to the application.

### Day 6: Testing Fundamentals

`@SpringBootTest`, `@MockBean` vs. `@Mock`, and test slices (`@WebMvcTest`, `@DataJpaTest`).

### Day 7: Consolidation

Write tests for everything built this week and review any shaky concepts.

## Week 2: Data and Spring Boot

### Day 8: JDBC Template Basics

`JdbcTemplate`, `RowMapper`, and exception translation.

### Day 9: Transactions: Propagation

`@Transactional` propagation types (`REQUIRED`, `REQUIRES_NEW`, `NESTED`, etc.). This is the usual weak spot, so do not rush it.

### Day 10: Transactions: Isolation and Rollback

`@Transactional` isolation levels, rollback rules, and the self-invocation pitfall (calling a `@Transactional` method from within the same class).

### Day 11: Spring Data JPA

Repositories, query derivation, `@Query`, pagination, and sorting.

### Day 12: Spring Boot Auto-Configuration

How `@EnableAutoConfiguration` works, conditional beans (`@ConditionalOnClass`, etc.), and starters.

### Day 13: Externalized Configuration

`application.yml`/properties, `@ConfigurationProperties`, profiles, and Actuator endpoints.

### Day 14: Consolidation

Convert the application fully to Spring Boot with a persistence layer; deliberately trigger a self-invocation transaction bug and fix it.

## Week 3: MVC, Security, and Exam Preparation

### Day 15: Spring MVC Controllers

`@RestController`, request mapping, `@RequestBody`/`@PathVariable`/`@RequestParam`.

### Day 16: Validation and Exception Handling

`@Valid`, custom validators, and exception handling via `@ControllerAdvice`.

### Day 17: Spring Security Basics

Filter chain, `UserDetailsService`, and method-level security (`@PreAuthorize`).

### Day 18: Spring Academy

Fill gaps on whatever has felt weakest so far.

### Day 19: Mock Exam #1

Review every wrong answer, not just the score.

### Day 20: Mock Exam #2

Targeted re-drill on Day 19's weak areas.

### Day 21: Mock Exam #3 and Final Review

Complete a final review pass on transaction propagation and bean scopes, the two highest-yield trouble spots.