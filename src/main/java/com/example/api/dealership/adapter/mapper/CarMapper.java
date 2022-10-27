package com.example.api.dealership.adapter.mapper;

import com.example.api.dealership.adapter.dtos.car.CarDtoRequest;
import com.example.api.dealership.adapter.dtos.car.CarDtoResponse;
import com.example.api.dealership.core.domain.CarModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Mapper(componentModel = "spring")
public interface CarMapper {

    @Mapping(expression = "java(getRegistrationDate())",target = "carRegistrationDate")
    CarModel toCarModel(CarDtoRequest carDtoRequest);


    default LocalDateTime getRegistrationDate(){
        return LocalDateTime.now(ZoneId.of("UTC"));
    }

    CarDtoResponse toCarDtoResponse(CarModel carModel);

}
