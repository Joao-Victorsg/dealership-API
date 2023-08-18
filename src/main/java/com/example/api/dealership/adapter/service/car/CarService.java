package com.example.api.dealership.adapter.service.car;

import com.example.api.dealership.adapter.dtos.car.CarDtoUpdateRequest;
import com.example.api.dealership.core.domain.CarModel;
import com.example.api.dealership.core.exceptions.CarNotFoundException;
import com.example.api.dealership.core.exceptions.DuplicatedInfoException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CarService {
    Optional<CarModel> findByVin(String vehicleIdentificationNumber) throws CarNotFoundException;

    CarModel save(CarModel car) throws DuplicatedInfoException;

    Page<CarModel> getCars(Pageable pageable, Double initialValue, Double finalValue, String Year, String color);

    void deleteCar(String vehicleIdentificationNumber) throws CarNotFoundException;

    CarModel updateCar(String vin, CarDtoUpdateRequest carUpdate) throws CarNotFoundException;
}
