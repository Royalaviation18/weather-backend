package com.royalaviation.weather.service;

import com.royalaviation.weather.dto.WeatherResponse;
import com.royalaviation.weather.model.Pincode;
import com.royalaviation.weather.model.WeatherData;
import com.royalaviation.weather.repository.PincodeRepository;
import com.royalaviation.weather.repository.WeatherDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class WeatherServiceImpl implements WeatherService {

    @Autowired
    private WeatherDataRepository weatherRepo;
    @Autowired
    private PincodeRepository pincodeRepo;
    @Autowired
    private GeoService geoService;

    @Override
    public WeatherResponse getWeather(String pincode, LocalDate date) {
        Optional<Pincode> pincodeOpt = pincodeRepo.findByPincode(pincode);

        Pincode pin = pincodeOpt.orElseGet(() -> geoService.fetchAndSaveCoordinates(pincode));

        Optional<WeatherData> existingWeather = weatherRepo.findByPincodeAndDate(pin, date);
        WeatherData data = existingWeather.orElseGet(() -> geoService.fetchAndSaveWeather(pin, date));

        return WeatherResponse.builder()
                .pincode(pincode)
                .date(date.toString())
                .temperature(data.getTemperature())
                .humidity(data.getHumidity())
                .description(data.getDescription())
                .build();
    }
}