package com.royalaviation.weather.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherRequest {
    private String pincode;
    private String date; // Format: yyyy-MM-dd
}