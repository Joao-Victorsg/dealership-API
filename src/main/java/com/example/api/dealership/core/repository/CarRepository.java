package com.example.api.dealership.core.repository;

import com.example.api.dealership.core.domain.CarModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CarRepository extends JpaRepository<CarModel, UUID> {

    Optional<CarModel> findByvehicleIdentificationNumber(String vehicleIdentificationNumber);

    void deleteByvehicleIdentificationNumber(String vehicleIdentificationNumber);

}
