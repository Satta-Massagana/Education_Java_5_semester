package ru.utmn.baranov.internet_availability.repository;

import org.springframework.data.repository.CrudRepository;
import ru.utmn.baranov.internet_availability.model.InternetAvailabilityModel;

public interface InternetAvailabilityJpaRepository extends CrudRepository<InternetAvailabilityModel, String> {
}