package com.example.api.dealership.adapter.output.repository.port;

import com.example.api.dealership.core.domain.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepositoryPort extends JpaRepository<UserModel, UUID> {
    Optional<UserModel> findByUsername(String username);
}
