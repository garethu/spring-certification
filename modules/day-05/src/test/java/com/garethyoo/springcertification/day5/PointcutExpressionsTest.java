package com.garethyoo.springcertification.day5;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PointcutExpressionsTest {
    @Autowired
    private ActivityLog activityLog;

    @Autowired
    private OrderService orderService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    void clearActivityLog() {
        activityLog.clear();
    }

    @Test
    void executionPointcutSelectsTheNamedServiceMethod() {
        orderService.placeOrder("Spring Core");

        assertThat(activityLog.entries()).containsExactly("execution:placeOrder");
    }

    @Test
    void withinPointcutSelectsMethodsDeclaredWithinTheTargetClass() {
        inventoryService.checkStock();
        inventoryService.updateStock();

        assertThat(activityLog.entries()).containsExactly("within:inventory", "within:inventory");
    }

    @Test
    void annotationPointcutSelectsOnlyAnnotatedMethods() {
        customerService.findCustomer(42);
        customerService.publicCatalog();

        assertThat(activityLog.entries()).containsExactly("@annotation:tracked");
    }

    @Test
    void argsPointcutSelectsMethodCallsWithAStringArgument() {
        notificationService.send("Order confirmed");
        notificationService.sendTo(42);

        assertThat(activityLog.entries()).containsExactly("args:String");
    }

    @Test
    void beanPointcutSelectsBeansBySpringBeanName() {
        orderRepository.loadRecent();

        assertThat(activityLog.entries()).containsExactly("bean:repository");
    }
}