package com.example.api.dealership.adapter.service.client.impl;

import com.example.api.dealership.adapter.service.client.ClientService;
import com.example.api.dealership.adapter.output.repository.port.ClientRepositoryPort;
import com.example.api.dealership.core.domain.ClientModel;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Optional;

import static com.example.api.dealership.adapter.output.repository.specifications.ClientSpecificationsFactory.equalCity;
import static com.example.api.dealership.adapter.output.repository.specifications.ClientSpecificationsFactory.equalState;

@RequiredArgsConstructor
@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepositoryPort clientRepositoryPort;

    @Override
    public Optional<ClientModel> findByCpf(String cpf) {
        return clientRepositoryPort.findByCpf(cpf);
    }

    @Override
    public Page<ClientModel> getClients(String city, String state, Pageable pageable) {

        final var specifications = new ArrayList<Specification<ClientModel>>();

        if(StringUtils.hasText(city))
            specifications.add(equalCity(city));

        if(StringUtils.hasText(state))
            specifications.add(equalState(state));

        final var specification = specifications.stream().reduce(Specification.where(null),Specification::and);

        return clientRepositoryPort.findAll(specification,pageable);
    }

    @Override
    @Transactional
    public ClientModel saveClient(ClientModel clientModel) {
        return clientRepositoryPort.save(clientModel);
    }

    @Override
    @Transactional
    public void deleteClient(String cpf) {
        clientRepositoryPort.deleteByCpf(cpf);
    }


}
