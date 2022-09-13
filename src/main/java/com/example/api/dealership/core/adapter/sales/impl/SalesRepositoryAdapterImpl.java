package com.example.api.dealership.core.adapter.sales.impl;

import com.example.api.dealership.core.adapter.sales.SalesRepositoryAdapter;
import com.example.api.dealership.core.domain.SalesModel;
import com.example.api.dealership.core.repository.SalesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class SalesRepositoryAdapterImpl implements SalesRepositoryAdapter {

    private final SalesRepository salesRepository;

    @Transactional
    @Override
    public SalesModel saveSale(SalesModel sale) {
        return salesRepository.save(sale);
    }

    @Override
    public Page<SalesModel> getSales(Pageable pageable) {
        return salesRepository.findAll(pageable);
    }

    @Override
    public Optional<SalesModel> findById(String id) {
        return salesRepository.findById(id);
    }

    @Transactional
    @Override
    public SalesModel updateSale(SalesModel sale) {
        return null;
    }

    @Override
    public void deleteSale(String id) {
        salesRepository.deleteById(id);
    }
}
