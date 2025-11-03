package ru.utmn.baranov.internet_availability.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.utmn.baranov.internet_availability.model.InternetAvailabilityModel;
import ru.utmn.baranov.internet_availability.repository.CommonRepository;

import java.util.Collection;
import java.util.stream.StreamSupport;

@Service
@Profile({"CsvEngine", "JdbcEngine"})
public class InternetAvailabilityService implements InternetAvailabilityServiceInterface {

    CommonRepository<InternetAvailabilityModel> csvRepository;

    public InternetAvailabilityService(
            CommonRepository<InternetAvailabilityModel> csvRepository,
            @Qualifier("CsvRepository") CommonRepository<InternetAvailabilityModel> jdbcRepository
    ) {
        this.csvRepository = csvRepository;

        if (jdbcRepository.getClass().equals(csvRepository.getClass())) {
            return;
        }

        if (jdbcRepository.count() > 0 && csvRepository.count() == 0) {
            Iterable<InternetAvailabilityModel> all = jdbcRepository.findAll();
            Collection<InternetAvailabilityModel> collection = StreamSupport.stream(all.spliterator(), false).toList();
            csvRepository.save(collection);
        }
    }

    public Iterable<InternetAvailabilityModel> getAll() {
        return csvRepository.findAll();
    }

    public InternetAvailabilityModel getOne(String id) {
        if (!csvRepository.exists(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Record not found");
        return csvRepository.findById(id);
    }

    public InternetAvailabilityModel add(InternetAvailabilityModel model) {
        if (csvRepository.exists(model.getCountryOrArea()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Record already exists");
        return csvRepository.save(model);
    }

    public void update(InternetAvailabilityModel model) {
        if (!csvRepository.exists(model.getCountryOrArea()))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Record not found");
        csvRepository.save(model);
    }

    public void delete(String id) {
        if (!csvRepository.exists(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Record not found");
        csvRepository.delete(id);
    }
}