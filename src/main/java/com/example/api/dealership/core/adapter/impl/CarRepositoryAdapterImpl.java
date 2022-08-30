package com.example.api.dealership.core.adapter.impl;

import com.example.api.dealership.core.adapter.CarRepositoryAdapter;
import com.example.api.dealership.core.domain.CarModel;
import com.example.api.dealership.core.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CarRepositoryAdapterImpl implements CarRepositoryAdapter {

    private final CarRepository carRepository;

    @Override
    public Optional<CarModel> findByvehicleIdentificationNumber(String vehicleIdentificationNumber) {
        return carRepository.findByvehicleIdentificationNumber(vehicleIdentificationNumber);
    }

    @Override
    @Transactional
    public CarModel save(CarModel car) {
        return carRepository.save(car);
    }

    @Override
    public Page<CarModel> getCars(Pageable pageable) {
        return carRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public void deleteCar(String vehicleIdentificationNumber) {
        carRepository.deleteByvehicleIdentificationNumber(vehicleIdentificationNumber);
    }

}
