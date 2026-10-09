package com.garethyoo.springcertification.lab;

import org.springframework.stereotype.Component;

@Component
// Study note: @Component marks this class for component scanning; its default scope is singleton.
public class InventoryService {

    public InventoryService() {
        System.out.println("[constructor] InventoryService");
    }

    public String description() {
        return "InventoryService was constructor-injected into CatalogService.";
    }
}