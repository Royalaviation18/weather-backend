package com.royalaviation.weather.repository;

import com.royalaviation.weather.model.Pincode;
import com.royalaviation.weather.model.WeatherData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface WeatherDataRepository extends JpaRepository<WeatherData, Long> {
    Optional<WeatherData> findByPincodeAndDate(Pincode pincode, LocalDate date);
}