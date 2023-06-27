package com.example.api.dealership.adapter.service.sales;

import com.example.api.dealership.core.domain.SalesModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;

public interface SalesService {

    SalesModel saveSale(SalesModel sale);

    Page<SalesModel> getSales(LocalDate initialDate, LocalDate finalDate,Pageable pageable);

    Optional<SalesModel> findById(String id);

    void deleteSale(String id);
}
