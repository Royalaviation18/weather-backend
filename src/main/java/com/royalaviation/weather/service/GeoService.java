package com.royalaviation.weather.service;

import com.royalaviation.weather.model.Pincode;
import com.royalaviation.weather.model.WeatherData;

import java.time.LocalDate;

public interface GeoService {
    Pincode fetchAndSaveCoordinates(String pincode);

    WeatherData fetchAndSaveWeather(Pincode pincode, LocalDate date);
}