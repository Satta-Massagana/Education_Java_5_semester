package ru.utmn.baranov.internet_availability.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import java.util.Collection;
import java.util.List;
import ru.utmn.baranov.internet_availability.model.InternetAvailabilityModel;
import ru.utmn.baranov.internet_availability.repository.CommonRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("CsvEngine")
class InternetAvailabilityServiceTest {

    // Arrange
    @Autowired
    @Qualifier("InternetAvailabilityServiceTest")
    InternetAvailabilityService service;

    @TestConfiguration
    static class InternetAvailabilityServiceImplTestContextConfiguration {

        static class MockRepository implements CommonRepository<InternetAvailabilityModel> {

            @Override
            public InternetAvailabilityModel save(InternetAvailabilityModel domain) {
                return null;
            }

            @Override
            public Iterable<InternetAvailabilityModel> save(Collection<InternetAvailabilityModel> domains) {
                return null;
            }

            @Override
            public void delete(String id) {}

            @Override
            public void delete(InternetAvailabilityModel domain) {}

            @Override
            public InternetAvailabilityModel findById(String id) {
                return null;
            }

            @Override
            public Iterable<InternetAvailabilityModel> findAll() {
                var e1 = new InternetAvailabilityModel();
                e1.setInternetUsers("100");

                var e2 = new InternetAvailabilityModel();
                e2.setInternetUsers("200");

                return List.of(e1, e2);
            }

            @Override
            public boolean exists(String id) {
                return false;
            }

            @Override
            public long count() {
                return 0;
            }
        }

        @Bean
        @Qualifier("InternetAvailabilityServiceTest")
        public InternetAvailabilityService getInternetAvailabilityService() {
            return new InternetAvailabilityService(null, null) {
                public void init(CommonRepository<InternetAvailabilityModel> jdbcRepository) {
                    csvRepository = new MockRepository();
                }
            };
        }
    }

    @Test
    void avgInternetUsersImplementTest() {
        // Act
        Long result = service.avgInternetUsers();

        // Assert
        assertEquals(result, 150, 0.00000001);
    }
}