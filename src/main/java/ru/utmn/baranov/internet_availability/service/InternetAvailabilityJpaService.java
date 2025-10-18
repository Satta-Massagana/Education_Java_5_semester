package ru.utmn.baranov.internet_availability.service;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.utmn.baranov.internet_availability.model.InternetAvailabilityModel;
import ru.utmn.baranov.internet_availability.repository.InternetAvailabilityJpaRepository;

@Service
@Profile("JpaEngine")
public class InternetAvailabilityJpaService implements InternetAvailabilityServiceInterface {

    private final InternetAvailabilityJpaRepository repository;

    public InternetAvailabilityJpaService(InternetAvailabilityJpaRepository repository) {
        this.repository = repository;
    }

    public Iterable<InternetAvailabilityModel> getAll() {
        return repository.findAll();
    }

    public InternetAvailabilityModel getOne(String id) {
        return repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Record not found")
        );
    }

    public InternetAvailabilityModel add(InternetAvailabilityModel model) {
        if (repository.existsById(model.getCountryOrArea()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Record already exists");
        return repository.save(model);
    }

    public void update(InternetAvailabilityModel model) {
        if (!repository.existsById(model.getCountryOrArea()))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Record not found");
        repository.save(model);
    }

    public void delete(String id) {
        if (!repository.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Record not found");
        repository.deleteById(id);
    }
}