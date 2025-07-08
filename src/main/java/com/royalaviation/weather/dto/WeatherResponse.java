package com.royalaviation.weather.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WeatherResponse {
    private String pincode;
    private String date;
    private Double temperature;
    private Integer humidity;
    private String description;
}