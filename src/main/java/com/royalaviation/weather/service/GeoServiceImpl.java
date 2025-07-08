package com.royalaviation.weather.service;

import com.royalaviation.weather.exception.ExternalApiException;
import com.royalaviation.weather.model.Pincode;
import com.royalaviation.weather.model.WeatherData;
import com.royalaviation.weather.repository.PincodeRepository;
import com.royalaviation.weather.repository.WeatherDataRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class GeoServiceImpl implements GeoService {

    @Value("${openweather.api.key}")
    private String apiKey;

    private final PincodeRepository pincodeRepo;
    private final WeatherDataRepository weatherRepo;
    private final RestTemplate restTemplate;

    public GeoServiceImpl(PincodeRepository pincodeRepo, WeatherDataRepository weatherRepo, RestTemplate restTemplate) {
        this.pincodeRepo = pincodeRepo;
        this.weatherRepo = weatherRepo;
        this.restTemplate = restTemplate;
    }

    @Override
    public Pincode fetchAndSaveCoordinates(String pincode) {
        String url = String.format("http://api.openweathermap.org/geo/1.0/zip?zip=%s,IN&appid=%s", pincode, apiKey);

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        if (response == null || !response.containsKey("lat") || !response.containsKey("lon")) {
            throw new ExternalApiException("Failed to fetch latitude/longitude for pincode: " + pincode);
        }

        Double lat = Double.parseDouble(response.get("lat").toString());
        Double lon = Double.parseDouble(response.get("lon").toString());

        Pincode pin = new Pincode();
        pin.setPincode(pincode);
        pin.setLatitude(lat);
        pin.setLongitude(lon);

        return pincodeRepo.save(pin);
    }

    @Override
    public WeatherData fetchAndSaveWeather(Pincode pincode, LocalDate date) {
        // this returns current weather even if date is in the past
        String url = String.format(
                "https://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&units=metric&appid=%s",
                pincode.getLatitude(), pincode.getLongitude(), apiKey
        );

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        if (response == null || !response.containsKey("main") || !response.containsKey("weather")) {
            throw new ExternalApiException("Failed to fetch weather data for pincode: " + pincode.getPincode());
        }

        Map<String, Object> main = (Map<String, Object>) response.get("main");
        List<Map<String, Object>> weatherList = (List<Map<String, Object>>) response.get("weather");

        if (main == null || weatherList == null || weatherList.isEmpty()) {
            throw new ExternalApiException("Incomplete weather data received from API.");
        }

        Double temperature = Double.parseDouble(main.get("temp").toString());
        Integer humidity = Integer.parseInt(main.get("humidity").toString());
        String description = weatherList.get(0).get("description").toString();

        WeatherData data = new WeatherData();
        data.setPincode(pincode);
        data.setDate(date);
        data.setTemperature(temperature);
        data.setHumidity(humidity);
        data.setDescription(description);

        return weatherRepo.save(data);
    }
}
