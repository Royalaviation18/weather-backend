package com.royalaviation.weather.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleExternalApiException_returnsBadGateway() {
        ResponseEntity<?> response = handler.handleExternalApiException(new ExternalApiException("API failure"));
        assertEquals(502, response.getStatusCodeValue());
    }

    @Test
    void handleGenericException_returnsInternalServerError() {
        ResponseEntity<?> response = handler.handleGenericException(new RuntimeException("Unknown error"));
        assertEquals(500, response.getStatusCodeValue());
    }
}
