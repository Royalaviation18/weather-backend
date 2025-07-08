package com.royalaviation.weather.controller;

import com.royalaviation.weather.dto.WeatherRequest;
import com.royalaviation.weather.dto.WeatherResponse;
import com.royalaviation.weather.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @PostMapping
    public ResponseEntity<?> getWeather(@RequestBody WeatherRequest request) {
        try {
            LocalDate date = LocalDate.parse(request.getDate());
            WeatherResponse data = weatherService.getWeather(request.getPincode(), date);
            return ResponseEntity.ok(data);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Invalid date format. Please use YYYY-MM-DD.");
        }
    }
}
