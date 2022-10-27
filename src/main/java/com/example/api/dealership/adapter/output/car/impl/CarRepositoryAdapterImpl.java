package com.example.api.dealership.adapter.output.car.impl;

import com.example.api.dealership.adapter.output.car.CarRepositoryAdapter;
import com.example.api.dealership.core.domain.CarModel;
import com.example.api.dealership.adapter.output.repository.CarRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CarRepositoryAdapterImpl implements CarRepositoryAdapter {

    private final CarRepositoryPort carRepositoryPort;

    @Override
    public Optional<CarModel> findByVin(String vehicleIdentificationNumber) {
        return carRepositoryPort.findBycarVin(vehicleIdentificationNumber);
    }

    @Override
    @Transactional
    public CarModel save(CarModel car) {
        return carRepositoryPort.save(car);
    }

    @Override
    public Page<CarModel> getCars(Pageable pageable) {
        return carRepositoryPort.findAll(pageable);
    }

    @Override
    @Transactional
    public void deleteCar(String vehicleIdentificationNumber) {
        carRepositoryPort.deleteBycarVin(vehicleIdentificationNumber);
    }

}
