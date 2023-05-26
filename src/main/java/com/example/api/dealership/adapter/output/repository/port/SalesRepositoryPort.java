package com.example.api.dealership.adapter.output.repository.port;

import com.example.api.dealership.core.domain.SalesModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesRepositoryPort extends JpaRepository<SalesModel, String>, JpaSpecificationExecutor<SalesModel> {
}
