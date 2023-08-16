/*
package com.example.api.dealership.adapter.service;

import com.example.api.dealership.adapter.output.repository.port.CarRepositoryPort;
import com.example.api.dealership.adapter.service.car.impl.CarServiceImpl;
import com.example.api.dealership.core.domain.CarModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.data.jpa.domain.Specification.where;

@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {

    @InjectMocks
    private CarServiceImpl carService;

    @Mock
    private CarRepositoryPort carRepositoryPort;


    @Test
    @DisplayName("Given an valid VIN return the car that have this VIN")
    void givenAnValidVinReturnTheCarThatHaveThisVin() {
        final var expectedCar = CarModel.builder().build();

        when(carRepositoryPort.findBycarVin("123")).thenReturn(Optional.of(expectedCar));

        final var car = carService.findByVin("123");

        verify(carRepositoryPort).findBycarVin("123");
        verifyNoMoreInteractions(carRepositoryPort);

        assertTrue(car.isPresent());
        assertEquals(car.get(),expectedCar);
    }

    @Test
    @DisplayName("Given an CarModel save it in the database")
    void givenAnValidCarModelSaveTheCarInTheDatabase() {
        final var expectedCar = CarModel.builder().build();

        when(carRepositoryPort.save(expectedCar)).thenReturn(expectedCar);

        final var car = carService.save(expectedCar);

        verify(carRepositoryPort).save(expectedCar);
        verifyNoMoreInteractions(carRepositoryPort);

        assertEquals(car,expectedCar);
    }

    @Test
    @DisplayName("Given an request without any filter return a page with cars")
    void givenAnRequestWithoutAnyFilterReturnAPageWithCars(){
        final var cars = getCarsList();
        final var expectedPage = new PageImpl<>(cars);
        final var pageable = PageRequest.of(0,10, Sort.by("id"));

        when(carRepositoryPort.findAll(where(null), pageable)).thenReturn(expectedPage);

        final var carsPage = carService.getCars(pageable,null,null,null,null);

        verify(carRepositoryPort).findAll(where(null),pageable);
        verifyNoMoreInteractions(carRepositoryPort);

        assertEquals(carsPage,expectedPage);
    }

    @Test
    @DisplayName("Given an request with value filter, return the cars that are between this values")
    void givenAnRequestWithValueFilterReturnTheCarsThatAreBetweenThisValue(){
        final var cars = getCarsList();
        final var expectedPage = new PageImpl<>(cars.subList(0,1));
        final var pageable = PageRequest.of(0,10, Sort.by("id"));
        final var initialValue = 30000.00;
        final var finalValue = 35000.00;

        when(carRepositoryPort.findAll(any(Specification.class), eq(pageable))).thenReturn(expectedPage);

        final var carsPage = carService.getCars(pageable,initialValue,finalValue,null,null);

        verify(carRepositoryPort).findAll(any(Specification.class),eq(pageable));
        verifyNoMoreInteractions(carRepositoryPort);

        assertEquals(carsPage,expectedPage);
    }

    @Test
    @DisplayName("Given an request with year filter, return the cars that are from this year")
    void givenAnRequestWithYearFilterReturnTheCarsThatAreFromThisYear(){
        final var cars = getCarsList();
        final var expectedPage = new PageImpl<>(List.of(cars.get(0)));
        final var pageable = PageRequest.of(0,10, Sort.by("id"));
        final var year = "1998";

        when(carRepositoryPort.findAll(any(Specification.class), eq(pageable))).thenReturn(expectedPage);

        final var carsPage = carService.getCars(pageable,null,null,year,null);

        verify(carRepositoryPort).findAll(any(Specification.class),eq(pageable));
        verifyNoMoreInteractions(carRepositoryPort);

        assertEquals(carsPage,expectedPage);
    }

    @Test
    @DisplayName("Given an request with color filter, return the cars that have this color")
    void givenAnRequestWithColorFilterReturnTheCarsThatHaveThisColor(){
        final var cars = getCarsList();
        final var expectedPage = new PageImpl<>(List.of(cars.get(2)));
        final var pageable = PageRequest.of(0,10, Sort.by("id"));
        final var color = "Yellow";

        when(carRepositoryPort.findAll(any(Specification.class), eq(pageable))).thenReturn(expectedPage);

        final var carsPage = carService.getCars(pageable,null,null,null,color);

        verify(carRepositoryPort).findAll(any(Specification.class),eq(pageable));
        verifyNoMoreInteractions(carRepositoryPort);

        assertEquals(carsPage,expectedPage);
    }

    @Test
    @DisplayName("Given an CarModel delete the car in the database")
    void deleteCar() {

        doNothing().when(carRepositoryPort).deleteBycarVin("123");

        carService.deleteCar("123");

        verify(carRepositoryPort).deleteBycarVin("123");
    }

    private List<CarModel> getCarsList(){
        return List.of(getCar("1998","Green",30000.00),
                getCar("1999","Green",35000.00),
                getCar("1999","Yellow",40000.00));
    }

    private CarModel getCar(String carModelYear, String carColor, Double carValue){
        return CarModel.builder()
                .carModelYear(carModelYear)
                .carColor(carColor)
                .carValue(carValue)
                .build();
    }
}*/
