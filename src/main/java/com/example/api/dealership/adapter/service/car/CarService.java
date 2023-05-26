package com.example.api.dealership.adapter.service.car;

import com.example.api.dealership.core.domain.CarModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CarService {
    Optional<CarModel> findByVin(String vehicleIdentificationNumber);

    CarModel save(CarModel car);

    Page<CarModel> getCars(Pageable pageable, Double initialValue, Double finalValue, String Year, String color);

    void deleteCar(String vehicleIdentificationNumber);

}
