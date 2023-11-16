package com.example.api.dealership.adapter.service.client.impl;

import com.example.api.dealership.adapter.dtos.client.ClientDtoUpdateRequest;
import com.example.api.dealership.adapter.output.gateway.SearchAddressGateway;
import com.example.api.dealership.adapter.output.repository.port.ClientRepositoryPort;
import com.example.api.dealership.adapter.service.client.ClientService;
import com.example.api.dealership.core.domain.ClientModel;
import com.example.api.dealership.core.exceptions.ClientNotFoundException;
import com.example.api.dealership.core.exceptions.DuplicatedInfoException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Optional;

import static com.example.api.dealership.adapter.output.repository.specifications.ClientSpecificationsFactory.equalCity;
import static com.example.api.dealership.adapter.output.repository.specifications.ClientSpecificationsFactory.equalState;

@RequiredArgsConstructor
@Service
@Slf4j
public class ClientServiceImpl implements ClientService {

    private final ClientRepositoryPort clientRepositoryPort;

    private final SearchAddressGateway searchAddressGateway;

    //TODO: Creating a Log Service or util to encapsulate the external technology

    @Override
    @Cacheable(value = "clients-by-cpf")
    public Optional<ClientModel> findByCpf(final String cpf) {
        return clientRepositoryPort.findByCpf(cpf);
    }

    @Override
    @Cacheable(value = "get-all-clients-sorted-by-id", key = "#root.target.generateKey(#city, #state, #pageable)")
    public Page<ClientModel> getClients(final String city, final String state, final Pageable pageable) {

        final var specifications = new ArrayList<Specification<ClientModel>>();

        if(StringUtils.hasText(city))
            specifications.add(equalCity(city));

        if(StringUtils.hasText(state))
            specifications.add(equalState(state));

        final var specification = specifications.stream().reduce(Specification.where(null),Specification::and);

        return clientRepositoryPort.findAll(specification,pageable);
    }

    public String generateKey(String city, String state, Pageable pageable) {
        String cityKey = city != null ? city : "nullCity";
        String stateKey = state != null ? state : "nullState";

        return cityKey + "_" + stateKey + "_" + pageable.getPageNumber() + "_" + pageable.getPageSize();
    }

    @Override
    @Transactional
    public ClientModel saveClient(final ClientModel client) throws DuplicatedInfoException {
        if(clientRepositoryPort.findByCpf(client.getCpf()).isPresent())
            throw new DuplicatedInfoException("A client with this CPF already exists");

        final var clientAddress = searchAddressGateway.byPostCode(client.getAddress()
                .getPostCode());

        BeanUtils.copyProperties(clientAddress,client.getAddress());

        return clientRepositoryPort.save(client);
    }

    @Override
    @Transactional
    public void deleteClient(final String cpf) throws ClientNotFoundException {
        if(clientRepositoryPort.findByCpf(cpf).isEmpty())
            throw new ClientNotFoundException("There isn't a client with this CPF");

        clientRepositoryPort.deleteByCpf(cpf);
    }

    @Override
    @Transactional
    public ClientModel updateClient(final String cpf, final ClientDtoUpdateRequest request) throws ClientNotFoundException {
        final var optionalClient = clientRepositoryPort.findByCpf(cpf);

        if(optionalClient.isEmpty())
            throw new ClientNotFoundException("A client with this CPF was not found");

        final var clientModel = optionalClient.get();

        final var clientAddress = searchAddressGateway.byPostCode(request.postCode());
        BeanUtils.copyProperties(clientAddress,clientModel.getAddress());

        //TODO: Create a constructor for ClientModel that receives another client.

        return clientRepositoryPort.save(clientModel);
    }

}