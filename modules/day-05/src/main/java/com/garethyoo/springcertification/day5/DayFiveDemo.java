package com.garethyoo.springcertification.day5;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DayFiveDemo implements CommandLineRunner {
    private final OrderService orderService;
    private final InventoryService inventoryService;
    private final CustomerService customerService;
    private final NotificationService notificationService;
    private final OrderRepository orderRepository;

    public DayFiveDemo(
            OrderService orderService,
            InventoryService inventoryService,
            CustomerService customerService,
            NotificationService notificationService,
            OrderRepository orderRepository) {
        this.orderService = orderService;
        this.inventoryService = inventoryService;
        this.customerService = customerService;
        this.notificationService = notificationService;
        this.orderRepository = orderRepository;
    }

    @Override
    public void run(String... args) {
        System.out.println("\n=== Day 5: AOP Pointcut Expressions Lab ===");
        System.out.println(orderService.placeOrder("Spring Core"));
        System.out.println(inventoryService.checkStock());
        System.out.println(inventoryService.updateStock());
        System.out.println(customerService.findCustomer(42));
        System.out.println(customerService.publicCatalog());
        System.out.println(notificationService.send("Order confirmed"));
        System.out.println(notificationService.sendTo(42));
        System.out.println(orderRepository.loadRecent());
        System.out.println("=== Day 5 demo complete ===\n");
    }
}