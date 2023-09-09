package com.example.api.dealership.adapter.service;

import com.example.api.dealership.adapter.mapper.SalesMapper;
import com.example.api.dealership.adapter.output.repository.port.SalesRepositoryPort;
import com.example.api.dealership.adapter.service.car.CarService;
import com.example.api.dealership.adapter.service.client.ClientService;
import com.example.api.dealership.adapter.service.sales.impl.SalesServiceImpl;
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
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SalesServiceImplTest {

    @InjectMocks
    private SalesServiceImpl salesService;

    @Mock
    private SalesRepositoryPort salesRepositoryPort;

    @Mock
    private ClientService clientService;

    @Mock
    private CarService carService;

    @Mock
    private SalesMapper salesMapper;

    @Test
    @DisplayName("Given a valid Sale save the sale in the database")
    void givenACarAndAClientSaveTheSaleInTheDatabase() {
        final var car = CarModel.builder().build();
        final var client = ClientModel.builder()
                .address(AddressModel.builder()
                        .isAddressSearched(true)
                        .build())
                .build();
        final var expectedsales = SalesModel.builder()
                .car(car)
                .client(client)
                .build();

        when(clientService.findByCpf("123")).thenReturn(Optional.of(client));
        when(carService.findByVin("123")).thenReturn(Optional.of(car));
        when(salesMapper.toSalesModel(car,client)).thenReturn(expectedsales);
        when(salesRepositoryPort.save(expectedsales)).thenReturn(expectedsales);

        assertDoesNotThrow(() -> {
            final var sales = salesService.saveSale("123","123");
            assertEquals(sales,expectedsales);
        }
        );
    }

    @Test
    @DisplayName("When trying to save a sale with a nonexistent client then throw ClientNotFoundException")
    void whenTryingToSaveSaleWithNonExistentClientThenThrowClientNotFoundException() {
        final var client = ClientModel.builder()
                .address(AddressModel.builder()
                        .isAddressSearched(true)
                        .build())
                .build();

        when(clientService.findByCpf("123")).thenReturn(Optional.of(client));
        when(carService.findByVin("123")).thenReturn(Optional.empty());

        assertThrows(CarNotFoundException.class,() -> salesService.saveSale("123","123"));
    }

    @Test
    @DisplayName("When trying to save a sale with a existent client without address then throw ClientNotHaveRegisteredAddressException")
    void whenTryingToSaveSaleWithNonExistentClientThenThrowClientNotHaveRegisteredAddressException() {
        final var client = ClientModel.builder()
                .address(AddressModel.builder()
                        .isAddressSearched(false)
                        .build())
                .build();
        final var car = CarModel.builder().build();

        when(clientService.findByCpf("123")).thenReturn(Optional.of(client));
        when(carService.findByVin("123")).thenReturn(Optional.of(car));

        assertThrows(ClientNotHaveRegisteredAddressException.class,() -> salesService.saveSale("123","123"));
    }

    @Test
    @DisplayName("When trying to save a sale with a nonexistent car then throw CarNotFoundException")
    void whenTryingToSaveSaleWithNonExistentCarThenThrowCarNotFoundException() {
        final var car = CarModel.builder().build();

        when(clientService.findByCpf("123")).thenReturn(Optional.empty());
        when(carService.findByVin("123")).thenReturn(Optional.of(car));

        assertThrows(ClientNotFoundException.class,() -> salesService.saveSale("123","123"));
    }

    @Test
    @DisplayName("When trying to save a sale with a car that were already sold, throw CarAlreadySoldException")
    void whenTryingToSaveSaleWitCarThatAlreadySoldThenThrowCarAlreadySoldException() {
        final var client = ClientModel.builder()
                .address(AddressModel.builder()
                        .isAddressSearched(true)
                        .build())
                .build();
        final var car = CarModel.builder().build();
        final var expectedsales = SalesModel.builder()
                .car(car)
                .client(client)
                .build();

        when(clientService.findByCpf("123")).thenReturn(Optional.of(client));
        when(carService.findByVin("123")).thenReturn(Optional.of(car));
        when(salesMapper.toSalesModel(car,client)).thenReturn(expectedsales);
        doThrow(new RuntimeException()).when(salesRepositoryPort).save(expectedsales);

        assertThrows(CarAlreadySoldException.class,() -> salesService.saveSale("123","123"));
    }

    @Test
    @DisplayName("Given a request without filters, return all the sales")
    void givenRequestWithoutFiltersReturnAllTheSales() {
        final var expectedsales = SalesModel.builder().build();
        final var expectedsalesPage = new PageImpl<>(List.of(expectedsales));
        final var pageable = PageRequest.of(0,10, Sort.by("id"));

        when(salesRepositoryPort.findAll(pageable)).thenReturn(expectedsalesPage);

        final var sales = salesService.getSales(null,null,pageable);

        assertEquals(sales,expectedsalesPage);
    }

    @Test
    @DisplayName("Given a request with data filters, return the sales that are between this dates")
    void givenRequestDataFiltersReturnTheSalesThatAreBetweenThisSales() {
        final var expectedsales = SalesModel.builder().registrationDate(LocalDateTime.now())
                .build();
        final var expectedsalesPage = new PageImpl<>(List.of(expectedsales));
        final var pageable = PageRequest.of(0,10, Sort.by("id"));

        when(salesRepositoryPort.findAll(any(Specification.class),eq(pageable))).thenReturn(expectedsalesPage);

        final var sales = salesService.getSales(LocalDate.of(2023,5,27),
                LocalDate.of(2023,5,27),
                pageable);

        assertEquals(sales,expectedsalesPage);
    }

    @Test
    @DisplayName("Given a id find the sales that have this id")
    void givenIdFindTheSalesThatHaveThisId() {
        final var expectedsales = SalesModel.builder().build();

        when(salesRepositoryPort.findById("123")).thenReturn(Optional.of(expectedsales));

        final var sales = salesService.findById("123");

        assertEquals(sales.get(),expectedsales);
    }

    @Test
    @DisplayName("Given a valid id delete the sale that have this id")
    void givenValidIdDeleteTheSaleThatHaveThisId() {

        when(salesRepositoryPort.findById("123")).thenReturn(Optional.of(SalesModel.builder().build()));
        doNothing().when(salesRepositoryPort).deleteById("123");

        assertDoesNotThrow(() ->salesService.deleteSale("123"));

        verify(salesRepositoryPort).deleteById("123");
    }

    @Test
    @DisplayName("Given a invalid id when deleting, then throw SalesNotFoundException")
    void givenInvalidIdWhenDeletingSalesThenThrowSalesNotFoundException() {
        when(salesRepositoryPort.findById("123")).thenReturn(Optional.empty());

        assertThrows(SaleNotFoundException.class,() ->salesService.deleteSale("123"));
    }

}
