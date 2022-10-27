package com.example.api.dealership.adapter.output.repository;

import com.example.api.dealership.core.domain.CarModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CarRepositoryPort extends JpaRepository<CarModel, UUID> {

    Optional<CarModel> findBycarVin(String vehicleIdentificationNumber);

    void deleteBycarVin(String vehicleIdentificationNumber);

}
