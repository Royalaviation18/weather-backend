package com.royalaviation.weather.repository;

import com.royalaviation.weather.model.Pincode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PincodeRepository extends JpaRepository<Pincode, Long> {
    Optional<Pincode> findByPincode(String pincode);
}