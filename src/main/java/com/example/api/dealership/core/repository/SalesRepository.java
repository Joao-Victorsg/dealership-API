package com.example.api.dealership.core.repository;

import com.example.api.dealership.core.domain.SalesModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalesRepository extends JpaRepository<SalesModel, String> {
}
