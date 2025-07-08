package com.royalaviation.weather.service;

import com.royalaviation.weather.dto.WeatherResponse;
import com.royalaviation.weather.model.Pincode;
import com.royalaviation.weather.model.WeatherData;
import com.royalaviation.weather.repository.PincodeRepository;
import com.royalaviation.weather.repository.WeatherDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class WeatherServiceImpl implements WeatherService {

    private static final Logger logger = LoggerFactory.getLogger(WeatherServiceImpl.class);

    private final WeatherDataRepository weatherRepo;
    private final PincodeRepository pincodeRepo;
    private final GeoService geoService;

    public WeatherServiceImpl(WeatherDataRepository weatherRepo, PincodeRepository pincodeRepo, GeoService geoService) {
        this.weatherRepo = weatherRepo;
        this.pincodeRepo = pincodeRepo;
        this.geoService = geoService;
    }

    @Override
    public WeatherResponse getWeather(String pincode, LocalDate date) {
        logger.info("Fetching weather for pincode: {}, date: {}", pincode, date);

        // Step 1: Checking if pincode exists in DB
        Optional<Pincode> pincodeOpt = pincodeRepo.findByPincode(pincode);

        // Step 2: If not, fetching and saving the data in db from OpenWeather
        Pincode pin = pincodeOpt.orElseGet(() -> {
            logger.info("Pincode not found in DB. Fetching from OpenWeather API...");
            return geoService.fetchAndSaveCoordinates(pincode);
        });

        // Step 3: Checking if weather data for given date exists
        Optional<WeatherData> existingWeather = weatherRepo.findByPincodeAndDate(pin, date);

        // Step 4: If not, fetching current weather (OpenWeather does not allow free historical data)
        WeatherData data = existingWeather.orElseGet(() -> {
            logger.info("Weather data not found in DB for date {}. Fetching from OpenWeather API...", date);
            return geoService.fetchAndSaveWeather(pin, date);
        });

        // Step 5: Response
        return WeatherResponse.builder()
                .pincode(pincode)
                .date(date.toString())
                .temperature(data.getTemperature())
                .humidity(data.getHumidity())
                .description(data.getDescription())
                .build();
    }
}
