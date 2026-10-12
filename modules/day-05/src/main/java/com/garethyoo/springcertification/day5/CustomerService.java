package com.garethyoo.springcertification.day5;

import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    @TrackAccess
    public String findCustomer(long id) {
        return "Customer " + id;
    }

    public String publicCatalog() {
        return "Public catalog";
    }
}