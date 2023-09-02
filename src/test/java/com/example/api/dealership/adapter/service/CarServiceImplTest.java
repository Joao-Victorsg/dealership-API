package com.example.api.dealership.adapter.service;

import com.example.api.dealership.adapter.dtos.car.CarDtoUpdateRequest;
import com.example.api.dealership.adapter.output.repository.port.CarRepositoryPort;
import com.example.api.dealership.adapter.service.car.impl.CarServiceImpl;
import com.example.api.dealership.core.domain.CarModel;
import com.example.api.dealership.core.exceptions.CarNotFoundException;
import com.example.api.dealership.core.exceptions.DuplicatedInfoException;
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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
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

        when(carRepositoryPort.findByVin("123")).thenReturn(Optional.of(expectedCar));

        final var car = carService.findByVin("123");

        assertTrue(car.isPresent());
        assertEquals(car.get(),expectedCar);
    }

    @Test
    @DisplayName("Given an CarModel save it in the database")
    void givenAnValidCarModelSaveTheCarInTheDatabase() {
        final var expectedCar = CarModel.builder().vin("123").build();

        when(carRepositoryPort.findByVin("123")).thenReturn(Optional.empty());
        when(carRepositoryPort.save(expectedCar)).thenReturn(expectedCar);

        assertDoesNotThrow(() -> {
            final var responseCar = carService.save(expectedCar);
            assertEquals(expectedCar,responseCar);
        }
        );
    }

    @Test
    @DisplayName("Given a repeated CarModel Throw Duplicated Info Exception")
    void givenRepeatedCarModelSaveTheCarInTheDatabase() {
        final var expectedCar = CarModel.builder().vin("123").build();

        when(carRepositoryPort.findByVin("123")).thenReturn(Optional.of(expectedCar));

        assertThrows(DuplicatedInfoException.class,() -> carService.save(expectedCar));
    }

    @Test
    @DisplayName("Given an request without any filter return a page with cars")
    void givenAnRequestWithoutAnyFilterReturnAPageWithCars(){
        final var cars = getCarsList();
        final var expectedPage = new PageImpl<>(cars);
        final var pageable = PageRequest.of(0,10, Sort.by("id"));

        when(carRepositoryPort.findAll(where(null), pageable)).thenReturn(expectedPage);

        final var carsPage = carService.getCars(pageable,null,null,null,null);

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

        assertEquals(carsPage,expectedPage);
    }

    @Test
    @DisplayName("Given a valid CarModel delete the car in the database")
    void givenValidCarDeleteItFromDatabase() {
        when(carRepositoryPort.findByVin("123"))
                .thenReturn(Optional.of(CarModel.builder().build()));

        doNothing().when(carRepositoryPort).deleteByVin("123");

        assertDoesNotThrow(() -> carService.deleteCar("123"));
        verify(carRepositoryPort).deleteByVin("123");
    }

    @Test
    @DisplayName("Given a nonexistent CarModel Throw CarNotFoundException")
    void givenNonexistentCarThrowCarNotFoundException() {
        when(carRepositoryPort.findByVin("123")).thenReturn(Optional.empty());

        assertThrows(CarNotFoundException.class,() -> carService.deleteCar("123"));
    }

    @Test
    @DisplayName("Given an existente VIN, update the car information")
    void givenExistenteVINUpdateTheCarInformation(){
        final var carUpdateDtoRequest = CarDtoUpdateRequest.builder()
                .value(11111.00)
                .color("red")
                .build();
        final var oldCar = CarModel.builder().vin("123456789").build();
        final var updatedCar = CarModel.builder().value(11111.00).color("red").build();

        when(carRepositoryPort.findByVin(oldCar.getVin())).thenReturn(Optional.of(oldCar));
        when(carRepositoryPort.save(any(CarModel.class))).thenReturn(updatedCar);

        assertDoesNotThrow(() -> {
            final var response = carService.updateCar(oldCar.getVin(),carUpdateDtoRequest);
            assertEquals(updatedCar,response);
        });
    }

    @Test
    @DisplayName("Given an nonexistente VIN, when trying to update the car information throw CarNotFoundException")
    void givenNonExistentVINWhenTryingToUpdateCarInformationThrowCarNotFoundException(){
        final var carUpdateDtoRequest = CarDtoUpdateRequest.builder()
                .value(11111.00)
                .color("red")
                .build();

        when(carRepositoryPort.findByVin("123456789")).thenReturn(Optional.empty());

        assertThrows(CarNotFoundException.class, () ->
                carService.updateCar("123456789",carUpdateDtoRequest));
    }


    private List<CarModel> getCarsList(){
        return List.of(getCar("1998","Green",30000.00),
                getCar("1999","Green",35000.00),
                getCar("1999","Yellow",40000.00));
    }

    private CarModel getCar(String carModelYear, String carColor, Double carValue){
        return CarModel.builder()
                .modelYear(carModelYear)
                .color(carColor)
                .value(carValue)
                .build();
    }
}
