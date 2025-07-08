package com.royalaviation.weather.service;

import com.royalaviation.weather.dto.WeatherResponse;
import com.royalaviation.weather.model.WeatherData;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public interface WeatherService {
    WeatherResponse getWeather(String pincode, LocalDate date);
}
