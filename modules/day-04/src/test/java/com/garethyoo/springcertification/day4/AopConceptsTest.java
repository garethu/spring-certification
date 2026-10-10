package com.garethyoo.springcertification.day4;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

@SpringBootTest
class AopConceptsTest {
    @Autowired
    private ReportOperations reportOperations;

    @Autowired
    private AdviceEventLog eventLog;

    @AfterEach
    void clearAdviceEvents() {
        eventLog.clear();
    }

    @Test
    void springBootExposesTheServiceThroughAnAopProxy() {
        assertThat(AopUtils.isAopProxy(reportOperations)).isTrue();
        assertThat(AopProxyUtils.ultimateTargetClass(reportOperations)).isEqualTo(ReportService.class);
    }

    @Test
    void successfulInvocationRunsBeforeAroundAndAfterReturningAdvice() {
        assertThat(reportOperations.generate("weekly-sales")).isEqualTo("Generated report: weekly-sales");

        assertThat(eventLog.events()).contains("before", "around-before", "around-after", "afterReturning", "after");
        assertThat(eventLog.events()).doesNotContain("afterThrowing");
    }

    @Test
    void failingInvocationRunsThrowingAdviceAndPropagatesTheException() {
        assertThatThrownBy(() -> reportOperations.generate(" "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Report name must not be blank");

        assertThat(eventLog.events()).contains("before", "around-before", "around-after", "afterThrowing", "after");
        assertThat(eventLog.events()).doesNotContain("afterReturning");
    }

    @Test
    void proxyConfigurationSelectsJdkOrCglibProxying() {
        try (AnnotationConfigApplicationContext jdkContext =
                     new AnnotationConfigApplicationContext(JdkProxyConfiguration.class);
             AnnotationConfigApplicationContext cglibContext =
                     new AnnotationConfigApplicationContext(CglibProxyConfiguration.class)) {
            Object jdkProxy = jdkContext.getBean(ReportOperations.class);
            Object cglibProxy = cglibContext.getBean(ReportService.class);

            assertThat(AopUtils.isJdkDynamicProxy(jdkProxy)).isTrue();
            assertThat(AopUtils.isCglibProxy(cglibProxy)).isTrue();
        }
    }

    @Configuration(proxyBeanMethods = false)
    @EnableAspectJAutoProxy(proxyTargetClass = false)
    @Import({ReportService.class, AdviceEventLog.class, ReportAuditAspect.class})
    static class JdkProxyConfiguration {
    }

    @Configuration(proxyBeanMethods = false)
    @EnableAspectJAutoProxy(proxyTargetClass = true)
    @Import({ReportService.class, AdviceEventLog.class, ReportAuditAspect.class})
    static class CglibProxyConfiguration {
    }
}