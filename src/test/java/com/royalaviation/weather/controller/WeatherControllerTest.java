package com.royalaviation.weather.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.royalaviation.weather.dto.WeatherRequest;
import com.royalaviation.weather.dto.WeatherResponse;
import com.royalaviation.weather.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WeatherController.class)
class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private WeatherService weatherService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getWeather_success() throws Exception {
        WeatherRequest request = new WeatherRequest("411014", "2020-10-15");
        WeatherResponse response = WeatherResponse.builder()
                .pincode("411014")
                .date("2020-10-15")
                .temperature(30.0)
                .humidity(60)
                .description("clear sky")
                .build();

        when(weatherService.getWeather("411014", LocalDate.parse("2020-10-15"))).thenReturn(response);

        mockMvc.perform(post("/weather")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.temperature").value(30.0));
    }

    @Test
    void getWeather_invalidDate() throws Exception {
        WeatherRequest request = new WeatherRequest("411014", "invalid-date");

        mockMvc.perform(post("/weather")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}