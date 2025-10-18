package ru.utmn.baranov.internet_availability.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.utmn.baranov.internet_availability.model.InternetAvailabilityModel;
import ru.utmn.baranov.internet_availability.service.InternetAvailabilityJpaService;
import ru.utmn.baranov.internet_availability.service.InternetAvailabilityService;
import ru.utmn.baranov.internet_availability.service.InternetAvailabilityServiceInterface;

@RestController
@RequestMapping("/api/internet-availability")
public class InternetAvailabilityController {

    private final InternetAvailabilityServiceInterface service;

    public InternetAvailabilityController(InternetAvailabilityServiceInterface internetAvailabilityService) {
        this.service = internetAvailabilityService;
    }

    @GetMapping
    public Iterable<InternetAvailabilityModel> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public InternetAvailabilityModel getOne(@PathVariable("id") String id) {
        return service.getOne(id);
    }

    @PostMapping
    public ResponseEntity<InternetAvailabilityModel> add(@RequestBody InternetAvailabilityModel model) {
        InternetAvailabilityModel saved = service.add(model);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody InternetAvailabilityModel model) {
        service.update(model);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") String id) {
        service.delete(id);
    }
}