package com.example.api.dealership.adapter.output.repository.adapter.sales;

import com.example.api.dealership.core.domain.SalesModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface SalesRepositoryAdapter {

    SalesModel saveSale(SalesModel sale);

    Page<SalesModel> getSales(Pageable pageable);

    Optional<SalesModel> findById(String id);

    SalesModel updateSale(SalesModel sale);

    void deleteSale(String id);
}
