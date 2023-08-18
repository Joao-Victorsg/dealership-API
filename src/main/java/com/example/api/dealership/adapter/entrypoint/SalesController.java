package com.example.api.dealership.adapter.entrypoint;

import com.example.api.dealership.adapter.dtos.Response;
import com.example.api.dealership.adapter.dtos.sales.SalesDtoRequest;
import com.example.api.dealership.adapter.dtos.sales.SalesDtoResponse;
import com.example.api.dealership.adapter.mapper.SalesMapper;
import com.example.api.dealership.adapter.service.sales.SalesService;
import com.example.api.dealership.core.exceptions.CarAlreadySoldException;
import com.example.api.dealership.core.exceptions.CarNotFoundException;
import com.example.api.dealership.core.exceptions.ClientNotFoundException;
import com.example.api.dealership.core.exceptions.ClientNotHaveRegisteredAddressException;
import com.example.api.dealership.core.exceptions.SaleNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.time.LocalDate;
import java.util.stream.Collectors;

import static com.example.api.dealership.adapter.dtos.Response.createResponse;

@Slf4j
@RestController
@RequestMapping("/v1/dealership")
@RequiredArgsConstructor
public class SalesController {

    private final SalesService salesService;

    private final SalesMapper salesMapper;

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
    public ResponseEntity<Response<SalesDtoResponse>> saveSale(@RequestBody @Valid final SalesDtoRequest request) throws ClientNotFoundException, CarAlreadySoldException, CarNotFoundException, ClientNotHaveRegisteredAddressException {

        var sale = salesService.saveSale(request.getCpf(), request.getVin());

        final var salesDtoResponse = createResponse(salesMapper.toSalesDtoResponse(sale));

        return ResponseEntity.created(URI.create("/v1/dealership/sales/" + sale.getId()))
                .body(salesDtoResponse);
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
    public ResponseEntity<Response<PageImpl<SalesDtoResponse>>> getAllSales(@PageableDefault(sort ="id",
            direction = Sort.Direction.ASC) final Pageable pageable, @RequestParam(required = false) final LocalDate initialDate,
                                                                         @RequestParam(required = false) final LocalDate finalDate){

        final var sales = salesService.getSales(initialDate, finalDate, pageable);

        final var salesDtoResponse = sales.stream()
                .map(salesMapper::toSalesDtoResponse)
                .collect(Collectors.toList());

        final var response = createResponse(new PageImpl<>(salesDtoResponse));

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
    public ResponseEntity<Response<SalesDtoResponse>> getSale(@PathVariable(value = "id") final String id) throws SaleNotFoundException {
        var sale = salesService.findById(id);

        if(sale.isEmpty()) throw new SaleNotFoundException("There isn't a sale with this id");

        final var response = createResponse(salesMapper.toSalesDtoResponse(sale.get()));

        return ResponseEntity.status(HttpStatus.OK).body(response);
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
    public ResponseEntity<Response<String>> deleteSales(@PathVariable(value = "id") final String id) throws SaleNotFoundException {
        salesService.deleteSale(id);

        log.info("The sale with ID: " + id + "was deleted successfully");

        final var response = createResponse("The sale with ID: " + id + " was deleted successfully");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
