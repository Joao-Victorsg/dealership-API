package com.example.api.dealership.adapter.entrypoint;

import com.example.api.dealership.adapter.dtos.Response;
import com.example.api.dealership.adapter.dtos.car.CarDtoRequest;
import com.example.api.dealership.adapter.dtos.car.CarDtoResponse;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
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
        final var carDto = new CarDtoRequest();
        final var carModel = new CarModel();
        final var carDtoResponse = new CarDtoResponse();
        final var response = new Response<>();
        response.setData(carDtoResponse);
        final var expectedResponse = ResponseEntity.created(URI.create("/v1/dealership/cars/" + carModel.getCarVin()))
                .body(response);

        when(carService.findByVin(carDto.getCarVin())).thenReturn(Optional.empty());
        when(carMapper.toCarModel(carDto)).thenReturn(carModel);
        when(carService.save(carModel)).thenReturn(carModel);
        when(carMapper.toCarDtoResponse(carModel)).thenReturn(carDtoResponse);

        final var savedCar = carController.saveCar(carDto);

        verify(carService).findByVin(carDto.getCarVin());
        verify(carMapper).toCarModel(carDto);
        verify(carService).save(carModel);
        verify(carMapper).toCarDtoResponse(carModel);

        assertEquals(HttpStatus.CREATED,savedCar.getStatusCode());
        assertEquals(expectedResponse.getBody().getData(),savedCar.getBody().getData());
    }

    @Test
    @DisplayName("Given a car request with a VIN that already exists, throw DuplicatedInfoException ")
    void givenCarRequestWithVinThatAlreadyExistsThrowDuplicatedInfoException() {
        final var carDto = new CarDtoRequest();
        final var carModel = new CarModel();

        when(carService.findByVin(carDto.getCarVin())).thenReturn(Optional.of(carModel));

        assertThrows(DuplicatedInfoException.class, () -> carController.saveCar(carDto));
    }

    @Test
    @DisplayName("Get a page of cars from the database")
    void getAPageOfCarsFromTheDatabase() {
        final var expectedCars = CarModel.builder().build();
        final var expectedCarsPage = new PageImpl<>(List.of(expectedCars));
        final var pageable = PageRequest.of(0,10, Sort.by("id"));
        final var carDtoResponse = new CarDtoResponse();
        final var expectedResponse = new PageImpl<>(List.of(carDtoResponse));


        when(carService.getCars(pageable,null,null,null,null)).thenReturn(expectedCarsPage);

        when(carMapper.toCarDtoResponse(expectedCars)).thenReturn(carDtoResponse);

        final var cars = carController.getAllCars(pageable,null,null,null,null);

        verify(carService).getCars(pageable,null,null,null,null);
        verify(carMapper).toCarDtoResponse(expectedCars);

        verifyNoMoreInteractions(carService,carMapper);

        assertEquals(HttpStatus.OK,cars.getStatusCode());
        assertEquals(expectedResponse,cars.getBody().getData());
    }

    @Test
    @DisplayName("Given a VIN get the car that have this VIN")
    void givenVinGetCarWithThisVin() throws CarNotFoundException {
        final var vin = "123";
        final var carModel = CarModel.builder().build();
        final var carDtoResponse = new CarDtoResponse();

        when(carService.findByVin(vin)).thenReturn(Optional.of(carModel));
        when(carMapper.toCarDtoResponse(carModel)).thenReturn(carDtoResponse);

        final var car = carController.getCarByVin(vin);

        verify(carService).findByVin(vin);
        verify(carMapper).toCarDtoResponse(carModel);

        verifyNoMoreInteractions(carService,carMapper);

        assertEquals(HttpStatus.OK,car.getStatusCode());
        assertEquals(carDtoResponse,car.getBody().getData());
    }

    @Test
    @DisplayName("Given a invalid VIN, throw CarNotFoundException")
    void givenInvalidVinThrowCarNotFoundException(){
        final var vin = "123";

        when(carService.findByVin(vin)).thenReturn(Optional.empty());

        assertThrows(CarNotFoundException.class,() ->carController.getCarByVin(vin));
    }

    @Test
    @DisplayName("Given a valid VIN, update the car info")
    void givenValidVinUpdateTheCarInfo() throws CarNotFoundException {
        final var vin = "123";
        final var carModel = CarModel.builder().carColor("Yellow").carValue(1000.00).build();
        final var updatedCarModel = CarModel.builder().carColor("Black").carValue(2000.00).build();
        final var carDtoRequest = new CarDtoRequest();
        final var carDtoResponse = new CarDtoResponse();
        carDtoResponse.setCarColor("Black");
        carDtoResponse.setCarValue(BigDecimal.valueOf(2000.00));

        when(carService.findByVin(vin)).thenReturn(Optional.of(carModel));
        when(carMapper.toCarModel(carDtoRequest)).thenReturn(updatedCarModel);
        when(carService.save(updatedCarModel)).thenReturn(updatedCarModel);
        when(carMapper.toCarDtoResponse(updatedCarModel)).thenReturn(carDtoResponse);

        final var updatedCar = carController.updateCar(vin,carDtoRequest);

        verify(carService).findByVin(vin);
        verify(carMapper).toCarModel(carDtoRequest);
        verify(carService).save(carModel);
        verify(carMapper).toCarDtoResponse(carModel);

        verifyNoMoreInteractions(carService,carMapper);

        assertEquals(HttpStatus.OK,updatedCar.getStatusCode());
        assertEquals(carDtoResponse,updatedCar.getBody().getData());
    }

    @Test
    @DisplayName("Given a invalid VIN, throw CarNotFoundException")
    void givenInvalidVinWhenUpdatingThrowCarNotFoundException() {
        final var vin = "123";
        final var carDtoRequest = new CarDtoRequest();

        when(carService.findByVin(vin)).thenReturn(Optional.empty());

        assertThrows(CarNotFoundException.class,() -> carController.updateCar(vin,carDtoRequest));
    }


    @Test
    @DisplayName("Given a valid VIN, delete the car")
    void givenValidVinDeleteTheCar() throws CarNotFoundException {
        final var vin = "123";
        final var carModel = CarModel.builder().carColor("Yellow").carValue(1000.00).build();
        final var expectedResponse = new Response<String>();
        expectedResponse.setData("Car with VIN: " + vin + " was deleted successfully");

        when(carService.findByVin(vin)).thenReturn(Optional.of(carModel));
        doNothing().when(carService).deleteCar(vin);

        final var response = carController.deleteCar(vin);

        verify(carService).findByVin(vin);
        verify(carService).deleteCar(vin);
        verifyNoMoreInteractions(carService);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(expectedResponse.getData(),response.getBody().getData());
    }

    @Test
    @DisplayName("Given a invalid VIN, throw a CarNotFoundException")
    void givenInvalidVinWhenDeletingThrowCarNotFoundException(){
        final var vin = "123";

        when(carService.findByVin(vin)).thenReturn(Optional.empty());

        assertThrows(CarNotFoundException.class,() -> carController.deleteCar(vin));
    }

}