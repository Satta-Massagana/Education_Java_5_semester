package ru.utmn.baranov.internet_availability.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("CsvEngine")
class InternetAvailabilityServiceTest {

    // Arrange
    @Autowired
    InternetAvailabilityService service;

    @Test
    void avgInternetUsersImplementTest() {
        // Act
        Long result = service.avgInternetUsers();

        // Assert
        assertTrue(result > 0);
    }
}