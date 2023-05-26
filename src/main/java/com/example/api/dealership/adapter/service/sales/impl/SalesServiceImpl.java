package com.example.api.dealership.adapter.service.sales.impl;

import com.example.api.dealership.adapter.service.sales.SalesService;
import com.example.api.dealership.adapter.output.repository.port.SalesRepositoryPort;
import com.example.api.dealership.core.domain.SalesModel;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static com.example.api.dealership.adapter.output.repository.specifications.SalesSpecificationsFactory.betweenDates;

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
    public Page<SalesModel> getSales(LocalDate initialDate, LocalDate finalDate,Pageable pageable) {

        if(ObjectUtils.allNotNull(initialDate,finalDate))
            return salesRepositoryPort.findAll(betweenDates(initialDate,finalDate),pageable);

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
