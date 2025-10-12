package ru.utmn.baranov.internet_availability.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.utmn.baranov.internet_availability.model.InternetAvailabilityModel;
import ru.utmn.baranov.internet_availability.repository.InternetAvailabilityCsvRepository;
import ru.utmn.baranov.internet_availability.repository.InternetAvailabilityJdbcRepository;

import java.util.Collection;
import java.util.stream.StreamSupport;

//@Service
public class InternetAvailabilityService {

    private final InternetAvailabilityCsvRepository csvRepository;
    private final InternetAvailabilityJdbcRepository jdbcRepository;

    public InternetAvailabilityService(InternetAvailabilityCsvRepository csvRepository,
                                       InternetAvailabilityJdbcRepository jdbcRepository) {
        this.csvRepository = csvRepository;
        this.jdbcRepository = jdbcRepository;

        if (jdbcRepository.count() == 0 && csvRepository.count() > 0) {
            Iterable<InternetAvailabilityModel> all = csvRepository.findAll();
            Collection<InternetAvailabilityModel> collection = StreamSupport.stream(all.spliterator(), false).toList();
            jdbcRepository.save(collection);
        }
    }

    public Iterable<InternetAvailabilityModel> getAll() {
        return jdbcRepository.findAll();
    }

    public InternetAvailabilityModel getOne(String id) {
        if (!jdbcRepository.exists(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Record not found");
        return jdbcRepository.findById(id);
    }

    public InternetAvailabilityModel add(InternetAvailabilityModel model) {
        if (jdbcRepository.exists(model.getCountryOrArea()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Record already exists");
        return jdbcRepository.save(model);
    }

    public void update(InternetAvailabilityModel model) {
        if (!jdbcRepository.exists(model.getCountryOrArea()))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Record not found");
        jdbcRepository.save(model);
    }

    public void delete(String id) {
        if (!jdbcRepository.exists(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Record not found");
        jdbcRepository.delete(id);
    }
}