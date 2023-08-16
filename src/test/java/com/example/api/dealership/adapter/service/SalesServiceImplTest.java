/*
package com.example.api.dealership.adapter.service;

import com.example.api.dealership.adapter.output.repository.port.SalesRepositoryPort;
import com.example.api.dealership.adapter.service.sales.impl.SalesServiceImpl;
import com.example.api.dealership.core.domain.SalesModel;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SalesServiceImplTest {

    @InjectMocks
    private SalesServiceImpl salesService;

    @Mock
    private SalesRepositoryPort salesRepositoryPort;

    @Test
    @DisplayName("Given a Sale save the sale in the database")
    void givenACarAndAClientSaveTheSaleInTheDatabase() {
        final var expectedsales = SalesModel.builder().build();

        when(salesRepositoryPort.save(expectedsales)).thenReturn(expectedsales);

        final var sales = salesService.saveSale(expectedsales);

        verify(salesRepositoryPort).save(expectedsales);
        verifyNoMoreInteractions(salesRepositoryPort);

        assertEquals(sales,expectedsales);
    }

    @Test
    @DisplayName("Given a request without filters, return all the sales")
    void givenRequestWithoutFiltersReturnAllTheSales() {
        final var expectedsales = SalesModel.builder().build();
        final var expectedsalesPage = new PageImpl<>(List.of(expectedsales));
        final var pageable = PageRequest.of(0,10, Sort.by("id"));

        when(salesRepositoryPort.findAll(pageable)).thenReturn(expectedsalesPage);

        final var sales = salesService.getSales(null,null,pageable);

        verify(salesRepositoryPort).findAll(pageable);
        verifyNoMoreInteractions(salesRepositoryPort);

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

        verify(salesRepositoryPort).findAll(any(Specification.class),eq(pageable));
        verifyNoMoreInteractions(salesRepositoryPort);

        assertEquals(sales,expectedsalesPage);
    }

    @Test
    @DisplayName("Given a id find the sales that have this id")
    void givenIdFindTheSalesThatHaveThisId() {
        final var expectedsales = SalesModel.builder().build();

        when(salesRepositoryPort.findById("123")).thenReturn(Optional.of(expectedsales));

        final var sales = salesService.findById("123");

        verify(salesRepositoryPort).findById("123");
        verifyNoMoreInteractions(salesRepositoryPort);

        assertEquals(sales.get(),expectedsales);
    }

    @Test
    @DisplayName("Given a valid id delete the sale that have this id")
    void givenValidIdDeleteTheSaleThatHaveThisId() {

        doNothing().when(salesRepositoryPort).deleteById("123");

        salesService.deleteSale("123");

        verify(salesRepositoryPort).deleteById("123");
        verifyNoMoreInteractions(salesRepositoryPort);
    }


}*/
