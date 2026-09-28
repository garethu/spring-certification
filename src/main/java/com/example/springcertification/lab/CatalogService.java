package com.example.springcertification.lab;

import org.springframework.stereotype.Component;

@Component
// Study note: singleton is the default scope, so repeated lookups return this same instance.
public class CatalogService {
    private final InventoryService inventoryService;

    // Study note: constructor injection makes the required dependency explicit and keeps the field final.
    public CatalogService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
        System.out.println("[constructor] CatalogService");
    }

    public String describeInventory() {
        return inventoryService.description();
    }
}