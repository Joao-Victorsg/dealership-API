package com.example.api.dealership.adapter.entrypoint;

import com.example.api.dealership.adapter.dtos.Response;
import com.example.api.dealership.adapter.dtos.car.CarDtoRequest;
import com.example.api.dealership.adapter.dtos.car.CarDtoResponse;
import com.example.api.dealership.adapter.mapper.CarMapper;
import com.example.api.dealership.adapter.output.repository.adapter.car.CarRepositoryAdapter;
import com.example.api.dealership.core.exceptions.CarNotFoundException;
import com.example.api.dealership.core.exceptions.DuplicatedInfoException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/dealership")
public class CarController {

    private final CarRepositoryAdapter carRepositoryAdapter;

    private final CarMapper carMapper;

    @Operation(summary = "Save a car in the database")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "The car was created with success"),
        @ApiResponse(responseCode = "400", description = "The server cannot process the request due to a client error"),
        @ApiResponse(responseCode = "408", description = "The request timed out"),
        @ApiResponse(responseCode = "409", description = "There was a conflict when creating the car"),
        @ApiResponse(responseCode = "500", description = "There was internal server erros"),
        @ApiResponse(responseCode = "502", description = "Bad Gateway, the server got a invalid response"),
        @ApiResponse(responseCode = "503", description = "The service is unaivalable"),
        @ApiResponse(responseCode = "504", description = "The Gateway timed out")
    })
    @PostMapping(path = "/cars", produces = "application/json")
    //@TokenValidator
    public ResponseEntity<Response<CarDtoResponse>> saveCar(@RequestBody @Valid CarDtoRequest carDto, @RequestHeader String token) throws DuplicatedInfoException {
        var response = new Response<CarDtoResponse>();

        var exists = carRepositoryAdapter.findByVin(carDto.getCarVin());

        if(exists.isEmpty()){
            var carModel = carRepositoryAdapter.save(carMapper.toCarModel(carDto));
            log.info("Creating car in the database: " + carModel);

            response.setData(carMapper.toCarDtoResponse(carModel));

            return ResponseEntity.created(URI.create("/v1/dealership/client/" + carModel.getCarVin()))
                    .body(response);
        }

        throw new DuplicatedInfoException("Already exists a car with this VIN");
    }

    @Operation(summary="Return a page of cars")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Return a list of cars"),
            @ApiResponse(responseCode = "400", description = "The server cannot process the request due to a client error"),
            @ApiResponse(responseCode = "408", description = "The request timed out"),
            @ApiResponse(responseCode = "500", description = "There was internal server erros"),
            @ApiResponse(responseCode = "503", description = "The service is unaivalable"),
            @ApiResponse(responseCode = "504", description = "The Gateway timed out")
    })
    @GetMapping(path="/cars",produces = "application/json")
    public ResponseEntity<Response<Page<CarDtoResponse>>> getAllCars(@PageableDefault(page = 1,size = 10, sort ="id",
            direction = Sort.Direction.ASC) Pageable pageable,@RequestHeader String token){

        var response = new Response<Page<CarDtoResponse>>();

        var cars = carRepositoryAdapter.getCars(pageable);

        response.setData(new PageImpl<>(
                cars.stream()
                        .map(carMapper::toCarDtoResponse)
                        .collect(Collectors.toList())
        ));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary="Return one car")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "The car was returned with success"),
            @ApiResponse(responseCode = "400", description = "The server cannot process the request due to a client error"),
            @ApiResponse(responseCode = "404",description = "There wasn't a car with the VIN that was informed."),
            @ApiResponse(responseCode = "408", description = "The request timed out"),
            @ApiResponse(responseCode = "500", description = "There was internal server erros"),
            @ApiResponse(responseCode = "503", description = "The service is unaivalable"),
            @ApiResponse(responseCode = "504", description = "The Gateway timed out")
    })
    @GetMapping(path="/cars/{vin}",produces = "application/json")
    public ResponseEntity<Response<CarDtoResponse>> getCarByVin(@PathVariable(value="vin") String vin,@RequestHeader String token) throws CarNotFoundException {

        var response = new Response<CarDtoResponse>();

        var cars = carRepositoryAdapter.findByVin(vin);

        if(cars.isPresent()){
            response.setData(carMapper.toCarDtoResponse(cars.get()));
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        throw new CarNotFoundException("There isn't a Car with this VIN");
    }

    @Operation(summary = "Update a car color or/and a Car Value")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "The car was update with success."),
            @ApiResponse(responseCode = "404",description = "There wasn't a car with the VIN that was informed."),
            @ApiResponse(responseCode = "408", description = "The request timed out"),
            @ApiResponse(responseCode = "502", description = "Bad Gateway, the server got a invalid response"),
            @ApiResponse(responseCode = "503", description = "The service is unaivalable"),
            @ApiResponse(responseCode = "504", description = "The Gateway timed out")
    })
    @PutMapping(path="/cars/{vin}",produces = "application/json")
    public ResponseEntity<Response<CarDtoResponse>> updateCar(@PathVariable(value = "vin") String vin, @RequestBody CarDtoRequest carDto,@RequestHeader String token) throws CarNotFoundException {

        var response = new Response<CarDtoResponse>();

        var carModelOptional = carRepositoryAdapter.findByVin(vin);

        if(carModelOptional.isPresent()){
            var carModel = carModelOptional.get();
            var carModelUpdate = carMapper.toCarModel(carDto);
            carModel.setCarColor(carModelUpdate.getCarColor());
            carModel.setCarValue(carModelUpdate.getCarValue());

            var request = carRepositoryAdapter.save(carModel);

            response.setData(carMapper.toCarDtoResponse(request));

            log.info("Updating car: " + carModel);

            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        throw new CarNotFoundException("There isn't a car with this VIN");
    }

    @Operation(summary = "Delete a car passing the VIN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "The car was deleted with success"),
            @ApiResponse(responseCode = "404",description = "There wasn't a car with the VIN that was informed in the database."),
            @ApiResponse(responseCode = "408", description = "The request timed out"),
            @ApiResponse(responseCode = "500", description = "There was internal server erros"),
            @ApiResponse(responseCode = "502", description = "Bad Gateway, the server got a invalid response"),
            @ApiResponse(responseCode = "503", description = "The service is unaivalable"),
            @ApiResponse(responseCode = "504", description = "The Gateway timed out")
    })
    @DeleteMapping(path = "/cars/{vin}", produces = "application/json")
    public ResponseEntity<Response<String>> deleteCar(@PathVariable(value = "vin") String vin,@RequestHeader String token) throws CarNotFoundException {

        var response = new Response<String>();

        var  carModelOptional = carRepositoryAdapter.findByVin(vin);

        if(carModelOptional.isPresent()){
            carRepositoryAdapter.deleteCar(vin);
            log.info("Deleted car with VIN: " + vin);
            response.setData("Car with VIN: " + vin + " was deleted successfully");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        throw new CarNotFoundException("There isn't a car with this Vin");
    }

}
