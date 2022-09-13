package com.example.api.dealership.core.adapter.car;

import com.example.api.dealership.core.domain.CarModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.util.Optional;

public interface CarRepositoryAdapter {
    Optional<CarModel> findByVin(String vehicleIdentificationNumber);

    CarModel save(CarModel car);

    Page<CarModel> getCars(Pageable pageable);

    void deleteCar(String vehicleIdentificationNumber);

}
