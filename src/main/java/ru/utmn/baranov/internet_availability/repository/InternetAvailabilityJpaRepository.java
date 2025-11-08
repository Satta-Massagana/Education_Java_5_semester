package ru.utmn.baranov.internet_availability.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.utmn.baranov.internet_availability.model.InternetAvailabilityModel;

public interface InternetAvailabilityJpaRepository extends CrudRepository<InternetAvailabilityModel, String> {

    // SQL
    @Query(value = "SELECT AVG(e.internetUsers) FROM internetAvailabilityModel e", nativeQuery = true)
    Long avgInternetUsers();

    // JPQL
    @Query("SELECT AVG(e.internetUsers) FROM InternetAvailabilityModel e")
    Long getAvgInternetUsers();
}