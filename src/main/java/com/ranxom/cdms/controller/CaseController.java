package com.ranxom.cdms.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class CaseController {

    @PostMapping
    public String createCase() {
        return "Case Created";
    }

    @GetMapping
    public String getCase() {
        return "Get Case";
    }

    @PutMapping
    public String updateCase() {
        return "Update Case";
    }

    @DeleteMapping
    public String deleteCase() {
        return "Delete Case";
    }
}