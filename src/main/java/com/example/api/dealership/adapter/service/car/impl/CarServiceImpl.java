package com.example.api.dealership.adapter.service.car.impl;

import com.example.api.dealership.adapter.dtos.car.CarDtoUpdateRequest;
import com.example.api.dealership.adapter.output.repository.port.CarRepositoryPort;
import com.example.api.dealership.adapter.service.car.CarService;
import com.example.api.dealership.core.domain.CarModel;
import com.example.api.dealership.core.exceptions.CarNotFoundException;
import com.example.api.dealership.core.exceptions.DuplicatedInfoException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Optional;

import static com.example.api.dealership.adapter.output.repository.specifications.CarSpecificationsFactory.betweenValues;
import static com.example.api.dealership.adapter.output.repository.specifications.CarSpecificationsFactory.equalColor;
import static com.example.api.dealership.adapter.output.repository.specifications.CarSpecificationsFactory.equalModelYear;

@RequiredArgsConstructor
@Service
public class CarServiceImpl implements CarService {

    private final CarRepositoryPort carRepositoryPort;

    @Override
    public Optional<CarModel> findByVin(String vehicleIdentificationNumber){
        return carRepositoryPort.findByVin(vehicleIdentificationNumber);
    }

    @Override
    @Transactional
    public CarModel save(CarModel car) throws DuplicatedInfoException {
        if(carRepositoryPort.findByVin(car.getVin()).isPresent())
            throw new DuplicatedInfoException("Already exists a car with this VIN");

        return carRepositoryPort.save(car);
    }

    @Override
    public Page<CarModel> getCars(final Pageable pageable, final Double initialValue, final Double finalValue, final String year, final String color) {
        final var specs = new ArrayList<Specification<CarModel>>();

        if(finalValue != null)
            specs.add(betweenValues(initialValue, finalValue));

        if(StringUtils.hasText(year))
            specs.add(equalModelYear(year));

        if(StringUtils.hasText(color))
            specs.add(equalColor(color));

        final var specification = specs.stream()
                .reduce(Specification.where(null),
                        Specification::and);

        return carRepositoryPort.findAll(specification,pageable);
    }

    @Override
    @Transactional
    public void deleteCar(String vehicleIdentificationNumber) throws CarNotFoundException {
        if(carRepositoryPort.findByVin(vehicleIdentificationNumber).isEmpty())
            throw new CarNotFoundException("There isn't a car with this VIN");

        carRepositoryPort.deleteByVin(vehicleIdentificationNumber);
    }

    @Override
    @Transactional
    public CarModel updateCar(final String vin, final CarDtoUpdateRequest carUpdate) throws CarNotFoundException {
        final var optinalCar = carRepositoryPort.findByVin(vin);

        if(optinalCar.isEmpty())
            throw new CarNotFoundException("There isn't a car with this VIN");
        //TODO: Create a mapper that do this update. Then create a new constructor in car model to validate
        // what have to be updated.
        final var car = optinalCar.get();
        BeanUtils.copyProperties(carUpdate,car);

        return carRepositoryPort.save(car);
    }

}
