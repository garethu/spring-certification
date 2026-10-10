package com.garethyoo.springcertification.day4;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ReportAuditAspect {
    private final AdviceEventLog eventLog;

    public ReportAuditAspect(AdviceEventLog eventLog) {
        this.eventLog = eventLog;
    }

    @Pointcut("execution(* com.garethyoo.springcertification.day4.ReportService.*(..))")
    private void reportOperation() {
    }

    @Before("reportOperation()")
    public void before() {
        eventLog.record("before");
    }

    @AfterReturning("reportOperation()")
    public void afterReturning() {
        eventLog.record("afterReturning");
    }

    @AfterThrowing("reportOperation()")
    public void afterThrowing() {
        eventLog.record("afterThrowing");
    }

    @After("reportOperation()")
    public void after() {
        eventLog.record("after");
    }

    @Around("reportOperation()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        eventLog.record("around-before");
        try {
            return joinPoint.proceed();
        } finally {
            eventLog.record("around-after");
        }
    }
}