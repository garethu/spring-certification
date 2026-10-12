package com.garethyoo.springcertification.day5;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ActivityLoggingAspect {
    private final ActivityLog activityLog;

    public ActivityLoggingAspect(ActivityLog activityLog) {
        this.activityLog = activityLog;
    }

    @Pointcut("execution(* com.garethyoo.springcertification.day5.OrderService.placeOrder(..))")
    private void placingOrder() {
    }

    @Pointcut("within(com.garethyoo.springcertification.day5.InventoryService)")
    private void inventoryOperation() {
    }

    @Pointcut("@annotation(com.garethyoo.springcertification.day5.TrackAccess)")
    private void trackedOperation() {
    }

    @Pointcut("execution(* com.garethyoo.springcertification.day5.NotificationService.*(..)) && args(String)")
    private void singleStringArgument() {
    }

    @Pointcut("bean(*Repository)")
    private void repositoryBean() {
    }

    @Before("placingOrder()")
    public void logOrder() {
        activityLog.record("execution:placeOrder");
    }

    @Before("inventoryOperation()")
    public void logInventoryOperation() {
        activityLog.record("within:inventory");
    }

    @Before("trackedOperation()")
    public void logTrackedOperation() {
        activityLog.record("@annotation:tracked");
    }

    @Before("singleStringArgument()")
    public void logStringArgument() {
        activityLog.record("args:String");
    }

    @Before("repositoryBean()")
    public void logRepositoryOperation() {
        activityLog.record("bean:repository");
    }
}