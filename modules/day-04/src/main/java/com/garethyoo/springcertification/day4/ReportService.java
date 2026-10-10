package com.garethyoo.springcertification.day4;

import org.springframework.stereotype.Service;

@Service
public class ReportService implements ReportOperations {
    @Override
    public String generate(String reportName) {
        if (reportName == null || reportName.isBlank()) {
            throw new IllegalArgumentException("Report name must not be blank");
        }
        return "Generated report: " + reportName;
    }
}