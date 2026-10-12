package com.garethyoo.springcertification.day5;

import org.springframework.stereotype.Service;

@Service
public class InventoryService {
    public String checkStock() {
        return "Stock checked";
    }

    public String updateStock() {
        return "Stock updated";
    }
}