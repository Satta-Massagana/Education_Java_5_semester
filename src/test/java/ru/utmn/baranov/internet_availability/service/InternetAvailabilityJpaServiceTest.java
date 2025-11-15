package ru.utmn.baranov.internet_availability.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import ru.utmn.baranov.internet_availability.model.InternetAvailabilityModel;
import ru.utmn.baranov.internet_availability.repository.InternetAvailabilityJpaRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InternetAvailabilityJpaServiceTest {

    @Mock
    private InternetAvailabilityJpaRepository csvRepository;

    @InjectMocks
    private InternetAvailabilityJpaService service;

    /*@BeforeEach
    void setUp() {
        csvRepository = Mockito.mock(InternetAvailabilityJpaRepository.class);
        service = new InternetAvailabilityJpaService(csvRepository);
    }*/

    InternetAvailabilityModel e1, e2;

    @BeforeEach
    void setUp() {
        e1 = new InternetAvailabilityModel();
        e1.setInternetUsers("100");

        e2 = new InternetAvailabilityModel();
        e2.setInternetUsers("200");
    }

    @Test
    void getAllTest() {
        // Arrange
        when(csvRepository.findAll()).thenReturn(List.of(e1, e2));

        // Act
        List<InternetAvailabilityModel> result = (List<InternetAvailabilityModel>) service.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        InternetAvailabilityModel e = result.get(0);
        assertEquals(100, e.getInternetUsers());
    }

    @Test
    void deleteTest() {
        when(csvRepository.existsById("1")).thenReturn(true);
        doNothing().when(csvRepository).deleteById("1");

        // Act
        service.delete("1");

        verify(csvRepository, times(1)).existsById("1");
        verify(csvRepository, times(1)).deleteById("1");
        verify(csvRepository, never()).findById("1");

        verifyNoMoreInteractions(csvRepository);
    }

    @Test
    void updateNotFoundTest() {
        when(csvRepository.existsById("999")).thenReturn(false);

        var e = new InternetAvailabilityModel();
        e.setCountryOrArea("999");

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> service.update(e));

        assertEquals(404, ex.getStatusCode().value());
        assertEquals("Record not found", ex.getReason());
        verify(csvRepository, times(1)).existsById("999");
    }
}