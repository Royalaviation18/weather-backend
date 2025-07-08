package com.royalaviation.weather.controller;

import com.royalaviation.weather.dto.WeatherRequest;
import com.royalaviation.weather.dto.WeatherResponse;
import com.royalaviation.weather.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @PostMapping
    public ResponseEntity<?> getWeather(@RequestBody WeatherRequest request) {
        LocalDate date = LocalDate.parse(request.getDate());
        WeatherResponse data = weatherService.getWeather(request.getPincode(), date);
        return ResponseEntity.ok(data);
    }
}
