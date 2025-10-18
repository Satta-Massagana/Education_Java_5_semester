package ru.utmn.baranov.internet_availability.service;

import ru.utmn.baranov.internet_availability.model.InternetAvailabilityModel;

public interface InternetAvailabilityServiceInterface {

    Iterable<InternetAvailabilityModel> getAll();

    InternetAvailabilityModel getOne(String id);

    InternetAvailabilityModel add(InternetAvailabilityModel model);

    void update(InternetAvailabilityModel model);

    void delete(String id);
}
