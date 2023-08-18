package com.example.api.dealership.adapter.service.sales;

import com.example.api.dealership.core.domain.SalesModel;
import com.example.api.dealership.core.exceptions.CarAlreadySoldException;
import com.example.api.dealership.core.exceptions.CarNotFoundException;
import com.example.api.dealership.core.exceptions.ClientNotFoundException;
import com.example.api.dealership.core.exceptions.ClientNotHaveRegisteredAddressException;
import com.example.api.dealership.core.exceptions.SaleNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;

public interface SalesService {

    SalesModel saveSale(String cpf, String vin) throws ClientNotFoundException, CarNotFoundException, ClientNotHaveRegisteredAddressException, CarAlreadySoldException;

    Page<SalesModel> getSales(LocalDate initialDate, LocalDate finalDate, Pageable pageable);

    Optional<SalesModel> findById(String id);

    void deleteSale(String id) throws SaleNotFoundException;
}
