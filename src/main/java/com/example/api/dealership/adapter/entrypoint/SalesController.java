package com.example.api.dealership.adapter.entrypoint;

import com.example.api.dealership.adapter.dtos.sales.SalesDtoRequest;
import com.example.api.dealership.adapter.dtos.sales.SalesDtoResponse;
import com.example.api.dealership.adapter.mapper.SalesMapper;
import com.example.api.dealership.adapter.output.car.CarRepositoryAdapter;
import com.example.api.dealership.adapter.output.client.ClientRepositoryAdapter;
import com.example.api.dealership.adapter.output.sales.SalesRepositoryAdapter;
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
@RestController
@RequestMapping("/v1/dealership")
@RequiredArgsConstructor
public class SalesController {

    private final SalesRepositoryAdapter salesRepositoryAdapter;

    private final SalesMapper salesMapper;

    private final ClientRepositoryAdapter clientRepositoryAdapter;

    private final CarRepositoryAdapter carRepositoryAdapter;

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
    private ResponseEntity<Object> saveSale(@RequestBody @Valid SalesDtoRequest request){
        var client = clientRepositoryAdapter.findByCpf(request.getCpf());

        var car = carRepositoryAdapter.findByVin(request.getVin());

        if(client.isEmpty() || car.isEmpty()){
           return ResponseEntity.notFound().build();
        }
        var sale = salesMapper.toSalesModel(car.get(),client.get());

        try {
            var salesModel = salesRepositoryAdapter.saveSale(sale);

            return ResponseEntity.created(URI.create("/v1/dealership/sales/" + sale.getId()))
                    .body(salesMapper.toSalesDtoResponse(salesModel));
        }
        catch (Exception ex){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("O carro já está vinculado a outra venda");
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
    private ResponseEntity<Page<SalesDtoResponse>> getAllSales(@PageableDefault(page = 0,size = 10, sort ="id",
            direction = Sort.Direction.ASC) Pageable pageable){

        var sales = salesRepositoryAdapter.getSales(pageable);

        var response = new PageImpl<>(sales.stream().map(sale -> salesMapper.toSalesDtoResponse(sale)).collect(Collectors.toList()));

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
    private ResponseEntity<Object> getSale(@PathVariable(value = "id") String id){
        var sale = salesRepositoryAdapter.findById(id);

        if(sale.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(salesMapper.toSalesDtoResponse(sale.get()));
        }

        //Deveria ser uma mensagem de exceção e não de corpo
        return ResponseEntity.notFound().build();
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
    public ResponseEntity<Object> deleteClient(@PathVariable(value = "id") String id){

        var sale = salesRepositoryAdapter.findById(id);

        if(sale.isPresent()){
            salesRepositoryAdapter.deleteSale(id);
            log.info("Deleted sale with ID: " + id);
            return ResponseEntity.status(HttpStatus.OK).body("Sale deleted successfully.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sale not found.");
    }
}
