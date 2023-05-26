package com.example.api.dealership.adapter.output.repository.port;

import com.example.api.dealership.core.domain.CarModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface CarRepositoryPort extends JpaRepository<CarModel, UUID>, JpaSpecificationExecutor<CarModel> {

    Optional<CarModel> findBycarVin(String vehicleIdentificationNumber);

    void deleteBycarVin(String vehicleIdentificationNumber);

}
