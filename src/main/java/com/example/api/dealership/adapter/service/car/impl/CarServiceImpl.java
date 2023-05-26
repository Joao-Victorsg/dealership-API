package com.example.api.dealership.adapter.service.car.impl;

import com.example.api.dealership.adapter.service.car.CarService;
import com.example.api.dealership.core.domain.CarModel;
import com.example.api.dealership.adapter.output.repository.port.CarRepositoryPort;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.api.dealership.adapter.output.repository.specifications.CarSpecificationsFactory.betweenValues;
import static com.example.api.dealership.adapter.output.repository.specifications.CarSpecificationsFactory.equalColor;
import static com.example.api.dealership.adapter.output.repository.specifications.CarSpecificationsFactory.equalModelYear;

@RequiredArgsConstructor
@Service
public class CarServiceImpl implements CarService {

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
    public Page<CarModel> getCars(Pageable pageable, Double initialValue, Double finalValue, String year, String color) {
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
    public void deleteCar(String vehicleIdentificationNumber) {
        carRepositoryPort.deleteBycarVin(vehicleIdentificationNumber);
    }

}
