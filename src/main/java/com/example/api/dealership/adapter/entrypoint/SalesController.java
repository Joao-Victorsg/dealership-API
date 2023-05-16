package com.example.api.dealership.adapter.entrypoint;

import com.example.api.dealership.adapter.dtos.Response;
import com.example.api.dealership.adapter.dtos.sales.SalesDtoRequest;
import com.example.api.dealership.adapter.dtos.sales.SalesDtoResponse;
import com.example.api.dealership.adapter.mapper.SalesMapper;
import com.example.api.dealership.adapter.service.car.CarService;
import com.example.api.dealership.adapter.service.client.ClientService;
import com.example.api.dealership.adapter.service.sales.SalesService;
import com.example.api.dealership.core.exceptions.CarAlreadySoldException;
import com.example.api.dealership.core.exceptions.CarNotFoundException;
import com.example.api.dealership.core.exceptions.ClientNotFoundException;
import com.example.api.dealership.core.exceptions.SaleNotFoundException;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/v1/dealership")
@RequiredArgsConstructor
public class SalesController {

    private final SalesService salesService;

    private final SalesMapper salesMapper;

    private final ClientService clientService;

    private final CarService carService;

    @Operation(summary="Save a sale")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "The sale was created with success"),
            @ApiResponse(responseCode = "400", description = "The server cannot process the request due to a client error"),
            @ApiResponse(responseCode = "408", description = "The request timed out"),
            @ApiResponse(responseCode = "409", description = "There was a conflict when creating the sale"),
            @ApiResponse(responseCode = "500", description = "There was internal server erros"),
            @ApiResponse(responseCode = "502", description = "Bad Gateway, the server got a invalid response"),
            @ApiResponse(responseCode = "503", description = "The service is unaivalable"),
            @ApiResponse(responseCode = "504", description = "The Gateway timed out")
    })
    @PostMapping(path = "/sales")
    private ResponseEntity<Response<SalesDtoResponse>> saveSale(@RequestBody @Valid SalesDtoRequest request) throws ClientNotFoundException, CarAlreadySoldException, CarNotFoundException {

        var response = new Response<SalesDtoResponse>();

        var client = clientService.findByCpf(request.getCpf());

        if(client.isEmpty()) throw new ClientNotFoundException("There isn't a client with this CPF");

        var car = carService.findByVin(request.getVin());

        if(car.isEmpty()) throw new CarNotFoundException("There isn't a car with this VIN");

        var sale = salesMapper.toSalesModel(car.get(),client.get());

        try {
            var salesModel = salesService.saveSale(sale);

            response.setData(salesMapper.toSalesDtoResponse(salesModel));

            return ResponseEntity.created(URI.create("/v1/dealership/sales/" + sale.getId()))
                    .body(response);
        }
        catch (Exception ex){
            throw new CarAlreadySoldException("The car with this VIN was already sold");
        }

    }

    @Operation(summary="Return a page of sales")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Return a list of sales"),
            @ApiResponse(responseCode = "400", description = "The server cannot process the request due to a client error"),
            @ApiResponse(responseCode = "408", description = "The request timed out"),
            @ApiResponse(responseCode = "500", description = "There was internal server erros"),
            @ApiResponse(responseCode = "503", description = "The service is unaivalable"),
            @ApiResponse(responseCode = "504", description = "The Gateway timed out")
    })
    @GetMapping(path= "/sales",produces = "application/json")
    private ResponseEntity<Response<Page<SalesDtoResponse>>> getAllSales(@PageableDefault(page = 0,size = 10, sort ="id",
            direction = Sort.Direction.ASC) Pageable pageable){

        var response = new Response<Page<SalesDtoResponse>>();

        var sales = salesService.getSales(pageable);

        response.setData(new PageImpl<>(sales.stream().map(salesMapper::toSalesDtoResponse).collect(Collectors.toList())));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary="Return one sale")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "The sale was returned with success"),
            @ApiResponse(responseCode = "400", description = "The server cannot process the request due to a client error"),
            @ApiResponse(responseCode = "404",description = "There wasn't a sale with the ID that was informed."),
            @ApiResponse(responseCode = "408", description = "The request timed out"),
            @ApiResponse(responseCode = "500", description = "There was internal server erros"),
            @ApiResponse(responseCode = "503", description = "The service is unaivalable"),
            @ApiResponse(responseCode = "504", description = "The Gateway timed out")
    })
    @GetMapping(path = "/sales/{id}", produces = "application/json")
    private ResponseEntity<Response<SalesDtoResponse>> getSale(@PathVariable(value = "id") String id) throws SaleNotFoundException {
        var response = new Response<SalesDtoResponse>();

        var sale = salesService.findById(id);

        if(sale.isPresent()){
            response.setData(salesMapper.toSalesDtoResponse(sale.get()));
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        throw new SaleNotFoundException("There isn't a sale with this id");
    }

    @Operation(summary = "Delete a sale passing the CPF")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "The sale was deleted with success"),
            @ApiResponse(responseCode = "404",description = "There wasn't a sale with the Id that was informed in the database."),
            @ApiResponse(responseCode = "408", description = "The request timed out"),
            @ApiResponse(responseCode = "500", description = "There was internal server erros"),
            @ApiResponse(responseCode = "502", description = "Bad Gateway, the server got a invalid response"),
            @ApiResponse(responseCode = "503", description = "The service is unaivalable"),
            @ApiResponse(responseCode = "504", description = "The Gateway timed out")
    })
    @DeleteMapping(path = "/sales/{id}", produces = "application/json")
    public ResponseEntity<Response<String>> deleteClient(@PathVariable(value = "id") String id) throws SaleNotFoundException {

        var response = new Response<String>();

        var sale = salesService.findById(id);

        if(sale.isPresent()){
            salesService.deleteSale(id);
            log.info("The sale with ID: " + id + "was deleted successfully");

            response.setData("The sale with ID: " + id + " was deleted successfully");

            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        throw new SaleNotFoundException("There isn't a sale with this id");
    }
}
