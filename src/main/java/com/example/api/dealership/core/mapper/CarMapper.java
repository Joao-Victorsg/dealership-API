package com.example.api.dealership.core.mapper;

import com.example.api.dealership.core.domain.CarModel;
import com.example.api.dealership.core.dtos.car.CarDtoRequest;
import com.example.api.dealership.core.dtos.car.CarDtoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Mapper(componentModel = "spring")
public interface CarMapper {

    @Mapping(expression = "java(getRegistrationDate())",target = "registrationDate")
    //@Mapping(source = "carDtoRequest.value",target = "carValue")
    CarModel toCarModel(CarDtoRequest carDtoRequest);


    default LocalDateTime getRegistrationDate(){
        return LocalDateTime.now(ZoneId.of("UTC"));
    }

    @Mapping(source="carModel.carValue",target = "value")
    CarDtoResponse toCarDtoResponse(CarModel carModel);

}
