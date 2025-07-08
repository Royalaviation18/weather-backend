package com.royalaviation.weather.dto;

import lombok.Data;

@Data
public class WeatherRequest {
    private String pincode;
    private String date; // Format: yyyy-MM-dd
}