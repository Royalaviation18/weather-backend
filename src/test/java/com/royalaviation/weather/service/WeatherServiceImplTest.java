package com.royalaviation.weather.service;

import com.royalaviation.weather.dto.WeatherResponse;
import com.royalaviation.weather.model.Pincode;
import com.royalaviation.weather.model.WeatherData;
import com.royalaviation.weather.repository.PincodeRepository;
import com.royalaviation.weather.repository.WeatherDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class WeatherServiceImplTest {

    private WeatherDataRepository weatherRepo;
    private PincodeRepository pincodeRepo;
    private GeoService geoService;
    private WeatherServiceImpl weatherService;

    @BeforeEach
    void setup() {
        weatherRepo = mock(WeatherDataRepository.class);
        pincodeRepo = mock(PincodeRepository.class);
        geoService = mock(GeoService.class);
        weatherService = new WeatherServiceImpl(weatherRepo, pincodeRepo, geoService);
    }

    @Test
    void shouldReturnWeatherResponse_whenPincodeAndWeatherDataExistInDb() {
        String pincode = "411014";
        LocalDate date = LocalDate.of(2020, 10, 15);

        Pincode pin = new Pincode();
        pin.setPincode(pincode);

        WeatherData data = new WeatherData();
        data.setPincode(pin);
        data.setDate(date);
        data.setTemperature(25.0);
        data.setHumidity(55);
        data.setDescription("clear sky");

        when(pincodeRepo.findByPincode(pincode)).thenReturn(Optional.of(pin));
        when(weatherRepo.findByPincodeAndDate(pin, date)).thenReturn(Optional.of(data));

        WeatherResponse response = weatherService.getWeather(pincode, date);

        assertNotNull(response);
        assertEquals("411014", response.getPincode());
        assertEquals("2020-10-15", response.getDate());
        assertEquals(25.0, response.getTemperature());
        assertEquals(55, response.getHumidity());
        assertEquals("clear sky", response.getDescription());
    }

    @Test
    void shouldFetchPincodeFromApi_whenPincodeNotInDb() {
        String pincode = "411014";
        LocalDate date = LocalDate.of(2020, 10, 15);

        Pincode pin = new Pincode();
        pin.setPincode(pincode);

        WeatherData data = new WeatherData();
        data.setPincode(pin);
        data.setDate(date);
        data.setTemperature(28.0);
        data.setHumidity(60);
        data.setDescription("sunny");

        when(pincodeRepo.findByPincode(pincode)).thenReturn(Optional.empty());
        when(geoService.fetchAndSaveCoordinates(pincode)).thenReturn(pin);
        when(weatherRepo.findByPincodeAndDate(pin, date)).thenReturn(Optional.of(data));

        WeatherResponse response = weatherService.getWeather(pincode, date);

        assertEquals("411014", response.getPincode());
        assertEquals(28.0, response.getTemperature());
    }

    @Test
    void shouldFetchWeatherFromApi_whenWeatherDataNotInDb() {
        String pincode = "411014";
        LocalDate date = LocalDate.of(2020, 10, 15);

        Pincode pin = new Pincode();
        pin.setPincode(pincode);

        WeatherData data = new WeatherData();
        data.setPincode(pin);
        data.setDate(date);
        data.setTemperature(29.5);
        data.setHumidity(70);
        data.setDescription("partly cloudy");

        when(pincodeRepo.findByPincode(pincode)).thenReturn(Optional.of(pin));
        when(weatherRepo.findByPincodeAndDate(pin, date)).thenReturn(Optional.empty());
        when(geoService.fetchAndSaveWeather(pin, date)).thenReturn(data);

        WeatherResponse response = weatherService.getWeather(pincode, date);

        assertEquals("partly cloudy", response.getDescription());
        assertEquals(29.5, response.getTemperature());
    }

    @Test
    void shouldFetchPincodeAndWeather_whenBothAreMissing() {
        String pincode = "411014";
        LocalDate date = LocalDate.of(2020, 10, 15);

        Pincode pin = new Pincode();
        pin.setPincode(pincode);

        WeatherData data = new WeatherData();
        data.setPincode(pin);
        data.setDate(date);
        data.setTemperature(31.2);
        data.setHumidity(52);
        data.setDescription("haze");

        when(pincodeRepo.findByPincode(pincode)).thenReturn(Optional.empty());
        when(geoService.fetchAndSaveCoordinates(pincode)).thenReturn(pin);
        when(weatherRepo.findByPincodeAndDate(pin, date)).thenReturn(Optional.empty());
        when(geoService.fetchAndSaveWeather(pin, date)).thenReturn(data);

        WeatherResponse response = weatherService.getWeather(pincode, date);

        assertEquals(31.2, response.getTemperature());
        assertEquals("haze", response.getDescription());
    }
}
