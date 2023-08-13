package com.example.api.dealership.adapter.entrypoint;

import com.example.api.dealership.adapter.dtos.Response;
import com.example.api.dealership.adapter.dtos.sales.SalesDtoRequest;
import com.example.api.dealership.adapter.dtos.sales.SalesDtoResponse;
import com.example.api.dealership.adapter.mapper.SalesMapper;
import com.example.api.dealership.adapter.service.car.CarService;
import com.example.api.dealership.adapter.service.client.ClientService;
import com.example.api.dealership.adapter.service.sales.SalesService;
import com.example.api.dealership.core.domain.AddressModel;
import com.example.api.dealership.core.domain.CarModel;
import com.example.api.dealership.core.domain.ClientModel;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SalesControllerTest {

    @InjectMocks
    private SalesController salesController;
    @Mock
    private  SalesService salesService;
    @Mock
    private  SalesMapper salesMapper;

    @Mock
    private ClientService clientService;

    @Mock
    private CarService carService;

    @Test
    @DisplayName("Given a sales valid request, save the sale")
    void givenSalesValidRequestSaveTheSale() throws CarAlreadySoldException, ClientNotFoundException, CarNotFoundException, ClientNotHaveRegisteredAddressException {
        final var salesDtoRequest = new SalesDtoRequest();
        salesDtoRequest.setCpf("123");
        salesDtoRequest.setVin("123");
        final var client = ClientModel.builder().address(AddressModel.builder().isAddressSearched(true).build()).build();
        final var car = CarModel.builder().build();
        final var sales = SalesModel.builder().build();
        final var salesDtoResponse= new SalesDtoResponse();

        when(clientService.findByCpf(salesDtoRequest.getCpf())).thenReturn(Optional.of(client));
        when(carService.findByVin(salesDtoRequest.getVin())).thenReturn(Optional.of(car));
        when(salesMapper.toSalesModel(car,client)).thenReturn(sales);
        when(salesService.saveSale(sales)).thenReturn(sales);
        when(salesMapper.toSalesDtoResponse(sales)).thenReturn(salesDtoResponse);

        final var resultSale = salesController.saveSale(salesDtoRequest);

        verify(clientService).findByCpf("123");
        verify(carService).findByVin("123");
        verify(salesMapper).toSalesModel(car,client);
        verify(salesService).saveSale(sales);
        verify(salesMapper).toSalesDtoResponse(sales);

        verifyNoMoreInteractions(clientService,carService,salesMapper);

        assertEquals(HttpStatus.CREATED,resultSale.getStatusCode());
        assertEquals(salesDtoResponse,resultSale.getBody().getData());
    }

    @Test
    @DisplayName("Given a sales valid request, but the client don't have a address, don't save the sale")
    void givenSalesValidRequestButClientDontHaveAddressThenDontSaveIt(){
        final var salesDtoRequest = new SalesDtoRequest();
        salesDtoRequest.setCpf("123");
        salesDtoRequest.setVin("123");
        final var client = ClientModel.builder().address(AddressModel.builder().isAddressSearched(false).build()).build();

        when(clientService.findByCpf(salesDtoRequest.getCpf())).thenReturn(Optional.of(client));

        assertThrows(ClientNotHaveRegisteredAddressException.class,() -> salesController.saveSale(salesDtoRequest));
        verify(clientService).findByCpf("123");
        verifyNoMoreInteractions(clientService);
    }

    @Test
    @DisplayName("Given a invalid CPF when saving a sale, throw ClientNotFoundException")
    void givenInvalidCpfWhenSavingSaleThrowClientNotFoundException(){
        final var salesDtoRequest = new SalesDtoRequest();
        salesDtoRequest.setCpf("321");
        salesDtoRequest.setVin("321");

        when(clientService.findByCpf(salesDtoRequest.getCpf())).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class,() ->salesController.saveSale(salesDtoRequest));
    }

    @Test
    @DisplayName("Given a invalid VIN when saving a sale, throw CarNotFoundException")
    void givenInvalidVinWhenSavingSaleThrowCarNotFoundException(){
        final var salesDtoRequest = new SalesDtoRequest();
        salesDtoRequest.setCpf("432");
        salesDtoRequest.setVin("432");
        final var client = ClientModel.builder().address(AddressModel.builder().isAddressSearched(true).build()).build();

        when(clientService.findByCpf(salesDtoRequest.getCpf())).thenReturn(Optional.of(client));
        when(carService.findByVin(salesDtoRequest.getVin())).thenReturn(Optional.empty());

        assertThrows(CarNotFoundException.class,() ->salesController.saveSale(salesDtoRequest));
    }

    @Test
    @DisplayName("Given a car that was solded, throw CarAlreadySoldException")
    void givenCarThatWasAlreadySoldThrowCarAlreadySoldException(){
        final var salesDtoRequest = new SalesDtoRequest();
        salesDtoRequest.setCpf("561");
        salesDtoRequest.setVin("561");
        final var client = ClientModel.builder().address(AddressModel.builder().isAddressSearched(true).build()).build();
        final var car = CarModel.builder().build();
        final var sales = SalesModel.builder().build();

        when(clientService.findByCpf(salesDtoRequest.getCpf())).thenReturn(Optional.of(client));
        when(carService.findByVin(salesDtoRequest.getVin())).thenReturn(Optional.of(car));
        when(salesMapper.toSalesModel(car,client)).thenReturn(sales);
        when(salesService.saveSale(sales)).thenThrow(RuntimeException.class);

        assertThrows(CarAlreadySoldException.class,() -> salesController.saveSale(salesDtoRequest));

        verify(clientService).findByCpf("561");
        verify(carService).findByVin("561");
        verify(salesMapper).toSalesModel(car,client);
        verify(salesService).saveSale(sales);

        verifyNoMoreInteractions(clientService,carService,salesMapper);
    }
    
    @Test
    @DisplayName("Get a page of sales")
    void getPageOfSales(){
        final var expectedSales = SalesModel.builder().build();
        final var expectedSalesPage = new PageImpl<>(List.of(expectedSales));
        final var pageable = PageRequest.of(0,10, Sort.by("id"));
        final var salesDtoResponse = new SalesDtoResponse();
        final var expectedResponse = new PageImpl<>(List.of(salesDtoResponse));

        when(salesService.getSales(null,null,pageable)).thenReturn(expectedSalesPage);
        when(salesMapper.toSalesDtoResponse(expectedSales)).thenReturn(salesDtoResponse);

        final var sales = salesController.getAllSales(pageable,null,null);

        verify(salesService).getSales(null,null,pageable);
        verify(salesMapper).toSalesDtoResponse(expectedSales);
        verifyNoMoreInteractions(salesService);

        assertEquals(HttpStatus.OK,sales.getStatusCode());
        assertEquals(expectedResponse,sales.getBody().getData());
    }


    @Test
    @DisplayName("Given a ID get the sales that have this ID")
    void givenIdGetSalesWithThisId() throws SaleNotFoundException {
        final var id = "123";
        final var salesModel = SalesModel.builder().build();
        final var salesDtoResponse = new SalesDtoResponse();

        when(salesService.findById(id)).thenReturn(Optional.of(salesModel));
        when(salesMapper.toSalesDtoResponse(salesModel)).thenReturn(salesDtoResponse);

        final var sales = salesController.getSale(id);

        verify(salesService).findById(id);
        verify(salesMapper).toSalesDtoResponse(salesModel);

        verifyNoMoreInteractions(salesService,salesMapper);

        assertEquals(HttpStatus.OK,sales.getStatusCode());
        assertEquals(salesDtoResponse,sales.getBody().getData());
    }

    @Test
    @DisplayName("Given a invalid ID, throw SaleNotFoundException")
    void givenInvalidIdThrowSalesNotFoundException(){
        final var id = "123";

        when(salesService.findById(id)).thenReturn(Optional.empty());

        assertThrows(SaleNotFoundException.class,() ->salesController.getSale(id));
    }

    @Test
    @DisplayName("Given a valid ID, delete the sales")
    void givenValidVinDeleteTheSales() throws SaleNotFoundException {
        final var id = "123";
        final var salesModel = SalesModel.builder().build();
        final var expectedResponse = new Response<String>();
        expectedResponse.setData("The sale with ID: " + id + " was deleted successfully");

        when(salesService.findById(id)).thenReturn(Optional.of(salesModel));
        doNothing().when(salesService).deleteSale(id);

        final var response = salesController.deleteSales(id);

        verify(salesService).findById(id);
        verify(salesService).deleteSale(id);
        verifyNoMoreInteractions(salesService);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(expectedResponse.getData(),response.getBody().getData());
    }

    @Test
    @DisplayName("Given a invalid ID, throw a SaleNotFoundException")
    void givenInvalidVinWhenDeletingThrowSalesNotFoundException(){
        final var id = "123";

        when(salesService.findById(id)).thenReturn(Optional.empty());

        assertThrows(SaleNotFoundException.class,() -> salesController.deleteSales(id));
    }

}