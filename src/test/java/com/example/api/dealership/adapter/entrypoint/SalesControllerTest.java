package com.example.api.dealership.adapter.entrypoint;

import com.example.api.dealership.adapter.dtos.Response;
import com.example.api.dealership.adapter.dtos.sales.SalesDtoRequest;
import com.example.api.dealership.adapter.dtos.sales.SalesDtoResponse;
import com.example.api.dealership.adapter.mapper.SalesMapper;
import com.example.api.dealership.adapter.service.sales.SalesService;
import com.example.api.dealership.core.domain.SalesModel;
import com.example.api.dealership.core.exceptions.CarAlreadySoldException;
import com.example.api.dealership.core.exceptions.CarNotFoundException;
import com.example.api.dealership.core.exceptions.ClientNotFoundException;
import com.example.api.dealership.core.exceptions.ClientNotHaveRegisteredAddressException;
import com.example.api.dealership.core.exceptions.SaleNotFoundException;
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

import java.util.List;
import java.util.Optional;

import static com.example.api.dealership.adapter.dtos.Response.createResponse;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SalesControllerTest {

    @InjectMocks
    private SalesController salesController;
    @Mock
    private  SalesService salesService;
    @Mock
    private  SalesMapper salesMapper;

/*    @Test
    @DisplayName("Given a sales valid request, save the sale")
    void givenSalesValidRequestSaveTheSale() throws CarAlreadySoldException, ClientNotFoundException, ClientNotHaveRegisteredAddressException, CarNotFoundException {
        final var salesDtoRequest = SalesDtoRequest.builder().vin("123").cpf("123").build();
        final var sales = SalesModel.builder().build();
        final var salesDtoResponse= SalesDtoResponse.builder().build();
        final var expectedResponse = ResponseEntity.created(URI.create("/v1/dealership/sales/" + sales.getId()))
                .body(createResponse(salesDtoResponse));

        when(salesService.saveSale(salesDtoRequest.cpf(),salesDtoRequest.vin())).thenReturn(sales);
        when(salesMapper.toSalesDtoResponse(sales)).thenReturn(salesDtoResponse);

        assertDoesNotThrow(() -> {
            final var resultSale = salesController.saveSale(salesDtoRequest);
            assertEquals(expectedResponse.getStatusCode(),resultSale.getStatusCode());
            assertEquals(expectedResponse.getBody().getData(),resultSale.getBody().getData());
        });
    }*/

    @Test
    @DisplayName("Given a sales valid request, but the client don't have a address, don't save the sale")
    void givenSalesValidRequestButClientDontHaveAddressThenDontSaveIt() throws CarAlreadySoldException, ClientNotFoundException, ClientNotHaveRegisteredAddressException, CarNotFoundException {
        final var salesDtoRequest = SalesDtoRequest.builder().cpf("123").vin("123").build();
        final var expectedResponse = ResponseEntity.badRequest()
                .body(createResponse("The client needs to have a registered address"));

        doThrow(ClientNotHaveRegisteredAddressException.class).when(salesService)
                .saveSale(salesDtoRequest.cpf(),salesDtoRequest.vin());

        assertThrows(ClientNotHaveRegisteredAddressException.class,() -> {
            final var response = salesController.saveSale(salesDtoRequest);
            assertEquals(expectedResponse.getStatusCode(),response.getStatusCode());
            assertEquals(expectedResponse.getBody().getData(),response.getBody().getData());
        });
    }

    @Test
    @DisplayName("Given a invalid CPF when saving a sale, throw ClientNotFoundException")
    void givenInvalidCpfWhenSavingSaleThrowClientNotFoundException() throws CarAlreadySoldException,
            ClientNotFoundException, ClientNotHaveRegisteredAddressException, CarNotFoundException {

        final var salesDtoRequest = SalesDtoRequest.builder().cpf("123").vin("123").build();
        final var expectedResponse = ResponseEntity.badRequest()
                .body(createResponse("There isn't a client with this CPF"));

        doThrow(ClientNotFoundException.class).when(salesService)
                .saveSale(salesDtoRequest.cpf(),salesDtoRequest.vin());

        assertThrows(ClientNotFoundException.class,() ->{
            final var response = salesController.saveSale(salesDtoRequest);
            assertEquals(expectedResponse.getStatusCode(),response.getStatusCode());
            assertEquals(expectedResponse.getBody().getData(),response.getBody().getData());
        });
    }

    @Test
    @DisplayName("Given a invalid VIN when saving a sale, throw CarNotFoundException")
    void givenInvalidVinWhenSavingSaleThrowCarNotFoundException() throws CarAlreadySoldException, ClientNotFoundException, ClientNotHaveRegisteredAddressException, CarNotFoundException {
        final var salesDtoRequest = SalesDtoRequest.builder().cpf("123").vin("123").build();
        final var expectedResponse = ResponseEntity.badRequest()
                .body(createResponse("There isn't a car with this VIN"));

        doThrow(CarNotFoundException.class).when(salesService).saveSale(salesDtoRequest.cpf(),
        salesDtoRequest.vin());

        assertThrows(CarNotFoundException.class,() ->{
            final var response = salesController.saveSale(salesDtoRequest);
            assertEquals(expectedResponse.getStatusCode(),response.getStatusCode());
            assertEquals(expectedResponse.getBody().getData(),response.getBody().getData());
        });
    }

    @Test
    @DisplayName("Given a car that was solded, throw CarAlreadySoldException")
    void givenCarThatWasAlreadySoldThrowCarAlreadySoldException() throws CarAlreadySoldException, ClientNotFoundException, ClientNotHaveRegisteredAddressException, CarNotFoundException {
        final var salesDtoRequest = SalesDtoRequest.builder().cpf("123").vin("123").build();
        final var expectedResponse = ResponseEntity.badRequest()
                .body(createResponse("The car with this VIN was already sold"));

        doThrow(CarAlreadySoldException.class).when(salesService).saveSale(salesDtoRequest.cpf(),
                salesDtoRequest.vin());

        assertThrows(CarAlreadySoldException.class,() ->{
            final var response = salesController.saveSale(salesDtoRequest);
            assertEquals(expectedResponse.getStatusCode(),response.getStatusCode());
            assertEquals(expectedResponse.getBody().getData(),response.getBody().getData());
        });
    }
    
    @Test
    @DisplayName("Get a page of sales")
    void getPageOfSales(){
        final var expectedSales = SalesModel.builder().build();
        final var expectedSalesPage = new PageImpl<>(List.of(expectedSales));
        final var pageable = PageRequest.of(0,10, Sort.by("id"));
        final var salesDtoResponse = SalesDtoResponse.builder().build();
        final var expectedResponse = ResponseEntity.status(HttpStatus.OK)
                .body(Response.createResponse(new PageImpl<>(List.of(salesDtoResponse))));

        when(salesService.getSales(null,null,pageable)).thenReturn(expectedSalesPage);
        when(salesMapper.toSalesDtoResponse(expectedSales)).thenReturn(salesDtoResponse);

        final var response = salesController.getAllSales(pageable,null,null);

        assertEquals(expectedResponse.getStatusCode(),response.getStatusCode());
        assertEquals(expectedResponse.getBody().getData(),response.getBody().getData());
    }

    @Test
    @DisplayName("Given a ID get the sales that have this ID")
    void givenIdGetSalesWithThisId(){
        final var id = "123";
        final var salesModel = SalesModel.builder().build();
        final var salesDtoResponse = SalesDtoResponse.builder().build();
        final var expectedResponse = ResponseEntity.status(HttpStatus.OK)
                .body(Response.createResponse(salesDtoResponse));

        when(salesService.findById(id)).thenReturn(Optional.of(salesModel));
        when(salesMapper.toSalesDtoResponse(salesModel)).thenReturn(salesDtoResponse);

        assertDoesNotThrow(() -> {
            final var response = salesController.getSale(id);
            assertEquals(expectedResponse.getStatusCode(),response.getStatusCode());
            assertEquals(expectedResponse.getBody().getData(),response.getBody().getData());
        });
    }

    @Test
    @DisplayName("Given a invalid ID, throw SaleNotFoundException")
    void givenInvalidIdThrowSalesNotFoundException(){
        final var id = "123";
        final var expectedResponse = ResponseEntity.badRequest()
                .body(Response.createResponse("There isn't a sale with this id"));

        when(salesService.findById(id)).thenReturn(Optional.empty());

        assertThrows(SaleNotFoundException.class,() -> {
            final var response = salesController.getSale(id);
            assertEquals(expectedResponse.getStatusCode(),response.getStatusCode());
            assertEquals(expectedResponse.getBody().getData(),response.getBody().getData());
        });
    }

    @Test
    @DisplayName("Given a valid ID, delete the sales")
    void givenValidVinDeleteTheSales() throws SaleNotFoundException {
        final var id = "123";
        final var expectedResponse = ResponseEntity.status(HttpStatus.OK)
                .body(Response.createResponse("The sale with ID: " + id + " was deleted successfully"));

        doNothing().when(salesService).deleteSale(id);

        final var response = salesController.deleteSales(id);

        assertEquals(expectedResponse.getStatusCode(),response.getStatusCode());
        assertEquals(expectedResponse.getBody().getData(),response.getBody().getData());
    }

    @Test
    @DisplayName("Given a invalid ID when deleting sales, throw a SaleNotFoundException")
    void givenInvalidVinWhenDeletingThrowSalesNotFoundException() throws SaleNotFoundException {
        final var id = "123";
        final var expectedResponse = ResponseEntity.badRequest()
                .body(Response.createResponse("There isn't a sale with this id"));

        doThrow(SaleNotFoundException.class).when(salesService).deleteSale(id);

        assertThrows(SaleNotFoundException.class,() -> {
            final var response = salesController.deleteSales(id);
            assertEquals(expectedResponse.getStatusCode(),response.getStatusCode());
            assertEquals(expectedResponse.getBody().getData(),response.getBody().getData());
        });
    }

}
