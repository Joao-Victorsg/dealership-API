package com.example.api.dealership.adapter.service.sales.impl;

import com.example.api.dealership.adapter.service.sales.SalesService;
import com.example.api.dealership.adapter.output.repository.port.SalesRepositoryPort;
import com.example.api.dealership.core.domain.SalesModel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SalesServiceImpl implements SalesService {

    private final SalesRepositoryPort salesRepositoryPort;

    @Transactional
    @Override
    public SalesModel saveSale(SalesModel sale) {
        return salesRepositoryPort.save(sale);
    }

    @Override
    public Page<SalesModel> getSales(Pageable pageable) {
        return salesRepositoryPort.findAll(pageable);
    }

    @Override
    public Optional<SalesModel> findById(String id) {
        return salesRepositoryPort.findById(id);
    }

    @Transactional
    @Override
    public SalesModel updateSale(SalesModel sale) {
        return null;
    }

    @Override
    public void deleteSale(String id) {
        salesRepositoryPort.deleteById(id);
    }
}
