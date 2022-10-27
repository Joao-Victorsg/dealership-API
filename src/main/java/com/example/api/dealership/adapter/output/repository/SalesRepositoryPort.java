package com.example.api.dealership.adapter.output.repository;

import com.example.api.dealership.core.domain.SalesModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalesRepositoryPort extends JpaRepository<SalesModel, String> {
}
