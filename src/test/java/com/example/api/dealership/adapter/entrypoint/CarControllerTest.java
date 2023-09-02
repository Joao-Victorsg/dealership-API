package com.example.api.dealership.adapter.entrypoint;

import com.example.api.dealership.adapter.dtos.Response;
import com.example.api.dealership.adapter.dtos.car.CarDtoRequest;
import com.example.api.dealership.adapter.dtos.car.CarDtoResponse;
import com.example.api.dealership.adapter.dtos.car.CarDtoUpdateRequest;
import com.example.api.dealership.adapter.mapper.CarMapper;
import com.example.api.dealership.adapter.service.car.CarService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarControllerTest {

    @InjectMocks
    private CarController carController;

    @Mock
    private CarService carService;

    @Mock
    private CarMapper carMapper;

    @Test
    @DisplayName("Given a valid car request, save it in the database")
    void givenValidCarRequestSaveItInTheDatabase() throws DuplicatedInfoException {
        final var carDtoRequest = CarDtoRequest.builder().build();
        final var carModel = CarModel.builder().build();
        final var carDtoResponse = CarDtoResponse.builder().build();
        final var expectedResponse = ResponseEntity.created(URI.create("/v1/dealership/cars/" + carModel.getVin()))
                .body(Response.createResponse(carDtoResponse));

        when(carMapper.toCarModel(carDtoRequest)).thenReturn(carModel);
        when(carService.save(carModel)).thenReturn(carModel);
        when(carMapper.toCarDtoResponse(carModel)).thenReturn(carDtoResponse);

        final var response = carController.saveCar(carDtoRequest);

        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertEquals(expectedResponse.getBody().getData(),response.getBody().getData());
    }

    @Test
    @DisplayName("Given a car request with a VIN that already exists, throw DuplicatedInfoException ")
    void givenCarRequestWithVinThatAlreadyExistsThrowDuplicatedInfoException() throws DuplicatedInfoException {
        final var carDto = CarDtoRequest.builder().build();
        final var carModel = CarModel.builder().build();

        when(carMapper.toCarModel(carDto)).thenReturn(carModel);
        doThrow(DuplicatedInfoException.class).when(carService).save(carModel);

        assertThrows(DuplicatedInfoException.class,() -> {
            final var response = carController.saveCar(carDto);
            assertEquals(HttpStatus.CONFLICT,response.getStatusCode());
        }
        );
    }

    @Test
    @DisplayName("Get a page of cars from the database")
    void getAPageOfCarsFromTheDatabase() {
        final var expectedCars = CarModel.builder().build();
        final var expectedCarsPage = new PageImpl<>(List.of(expectedCars));
        final var pageable = PageRequest.of(0,10, Sort.by("id"));
        final var carDtoResponse = CarDtoResponse.builder().build();
        final var expectedResponse = new PageImpl<>(List.of(carDtoResponse));

        when(carService.getCars(pageable,null,null,null,null)).thenReturn(expectedCarsPage);
        when(carMapper.toCarDtoResponse(expectedCars)).thenReturn(carDtoResponse);

        final var cars = carController.getAllCars(pageable,null,null,null,null);

        assertEquals(HttpStatus.OK,cars.getStatusCode());
        assertEquals(expectedResponse,cars.getBody().getData());
    }

    @Test
    @DisplayName("Given a VIN get the car that have this VIN")
    void givenVinGetCarWithThisVin(){
        final var vin = "123";
        final var carModel = CarModel.builder().build();
        final var carDtoResponse = CarDtoResponse.builder().build();

        when(carService.findByVin(vin)).thenReturn(Optional.of(carModel));
        when(carMapper.toCarDtoResponse(carModel)).thenReturn(carDtoResponse);

        assertDoesNotThrow(() -> {
                    final var car = carController.getCarByVin(vin);
                    assertEquals(HttpStatus.OK, car.getStatusCode());
                    assertEquals(carDtoResponse, car.getBody().getData());
                }
        );
    }

    @Test
    @DisplayName("Given a invalid VIN, throw CarNotFoundException")
    void givenInvalidVinThrowCarNotFoundException(){
        final var vin = "123";

        when(carService.findByVin(vin)).thenReturn(Optional.empty());

        assertThrows(CarNotFoundException.class, () -> {
                    final var response = carController.getCarByVin(vin);
                    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
                    assertEquals("There isn't a Car with this VIN", response.getBody().getData());
                }
        );
    }

    @Test
    @DisplayName("Given a valid VIN, update the car info")
    void givenValidVinUpdateTheCarInfo() throws CarNotFoundException {
        final var vin = "123";
        final var updatedCarModel = CarModel.builder().color("Black").value(2000.00).build();
        final var carDtoRequest = CarDtoUpdateRequest.builder().build();
        final var carDtoResponse = CarDtoResponse.builder()
                .color("Black")
                .value(BigDecimal.valueOf(2000.00))
                .build();

        when(carService.updateCar(vin,carDtoRequest)).thenReturn(updatedCarModel);
        when(carMapper.toCarDtoResponse(updatedCarModel)).thenReturn(carDtoResponse);

        assertDoesNotThrow(() -> {
                    final var response = carController.updateCar(vin, carDtoRequest);
                    assertEquals(HttpStatus.OK, response.getStatusCode());
                    assertEquals(carDtoResponse, response.getBody().getData());
                }
        );
    }

    @Test
    @DisplayName("Given a invalid VIN, throw CarNotFoundException")
    void givenInvalidVinWhenUpdatingThrowCarNotFoundException() throws CarNotFoundException {
        final var vin = "123";
        final var carDtoRequest = CarDtoUpdateRequest.builder().build();

        doThrow(CarNotFoundException.class).when(carService).updateCar(vin,carDtoRequest);

        assertThrows(CarNotFoundException.class,() -> carController.updateCar(vin,carDtoRequest));
    }


    @Test
    @DisplayName("Given a valid VIN, delete the car")
    void givenValidVinDeleteTheCar() throws CarNotFoundException {
        final var vin = "123";
        final var expectedResponse = Response.createResponse("Car with VIN: " + vin + " was deleted successfully");

        doNothing().when(carService).deleteCar(vin);

        assertDoesNotThrow(() -> {
                    final var response = carController.deleteCar(vin);
                    assertEquals(HttpStatus.OK, response.getStatusCode());
                    assertEquals(expectedResponse.getData(), response.getBody().getData());
                }
        );
    }

    @Test
    @DisplayName("Given a invalid VIN, throw a CarNotFoundException")
    void givenInvalidVinWhenDeletingThrowCarNotFoundException() throws CarNotFoundException {
        final var vin = "123";

        doThrow(new CarNotFoundException("There isn't a car with this VIN")).when(carService).deleteCar(vin);

        assertThrows(CarNotFoundException.class,() -> carController.deleteCar(vin));
    }
}
