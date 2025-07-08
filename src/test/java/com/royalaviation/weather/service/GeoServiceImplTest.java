package com.royalaviation.weather.service;

import com.royalaviation.weather.exception.ExternalApiException;
import com.royalaviation.weather.model.Pincode;
import com.royalaviation.weather.repository.PincodeRepository;
import com.royalaviation.weather.repository.WeatherDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class GeoServiceImplTest {

    @Mock
    private PincodeRepository pincodeRepo;

    @Mock
    private WeatherDataRepository weatherRepo;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private GeoServiceImpl geoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should successfully fetch and save coordinates from API")
    void fetchAndSaveCoordinates_success() {
        Map<String, Object> mockResponse = Map.of("lat", 18.5, "lon", 73.8);
        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(mockResponse);

        Pincode saved = new Pincode();
        saved.setLatitude(18.5);
        saved.setLongitude(73.8);
        saved.setPincode("411014");

        when(pincodeRepo.save(any(Pincode.class))).thenReturn(saved);

        Pincode result = geoService.fetchAndSaveCoordinates("411014");

        assertNotNull(result);
        assertEquals("411014", result.getPincode());
        assertEquals(18.5, result.getLatitude());
        assertEquals(73.8, result.getLongitude());
    }

    @Test
    @DisplayName("Should throw ExternalApiException when API returns null")
    void fetchAndSaveCoordinates_apiReturnsNull() {
        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(null);
        assertThrows(ExternalApiException.class, () -> geoService.fetchAndSaveCoordinates("411014"));
    }

    @Test
    @DisplayName("Should throw ExternalApiException when API response is missing keys")
    void fetchAndSaveCoordinates_incompleteResponse() {
        Map<String, Object> incompleteResponse = Map.of("lat", 18.5); // missing lon
        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(incompleteResponse);
        assertThrows(ExternalApiException.class, () -> geoService.fetchAndSaveCoordinates("411014"));
    }
}
