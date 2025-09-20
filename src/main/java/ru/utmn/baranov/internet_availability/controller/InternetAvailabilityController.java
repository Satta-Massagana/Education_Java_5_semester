package ru.utmn.baranov.internet_availability.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InternetAvailabilityController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello Word!";
    }
}