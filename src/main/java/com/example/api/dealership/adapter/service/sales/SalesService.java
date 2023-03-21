package com.example.api.dealership.adapter.service.sales;

import com.example.api.dealership.core.domain.SalesModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface SalesService {

    SalesModel saveSale(SalesModel sale);

    Page<SalesModel> getSales(Pageable pageable);

    Optional<SalesModel> findById(String id);

    SalesModel updateSale(SalesModel sale);

    void deleteSale(String id);
}
