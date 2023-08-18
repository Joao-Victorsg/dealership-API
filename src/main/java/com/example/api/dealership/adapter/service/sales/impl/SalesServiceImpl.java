package com.example.api.dealership.adapter.service.sales.impl;

import com.example.api.dealership.adapter.mapper.SalesMapper;
import com.example.api.dealership.adapter.output.repository.port.SalesRepositoryPort;
import com.example.api.dealership.adapter.service.car.CarService;
import com.example.api.dealership.adapter.service.client.ClientService;
import com.example.api.dealership.adapter.service.sales.SalesService;
import com.example.api.dealership.core.domain.SalesModel;
import com.example.api.dealership.core.exceptions.CarAlreadySoldException;
import com.example.api.dealership.core.exceptions.CarNotFoundException;
import com.example.api.dealership.core.exceptions.ClientNotFoundException;
import com.example.api.dealership.core.exceptions.ClientNotHaveRegisteredAddressException;
import com.example.api.dealership.core.exceptions.SaleNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

import static com.example.api.dealership.adapter.output.repository.specifications.SalesSpecificationsFactory.betweenDates;

@RequiredArgsConstructor
@Service
public class SalesServiceImpl implements SalesService {

    private final SalesRepositoryPort salesRepositoryPort;

    private final ClientService clientService;

    private final CarService carService;

    private final SalesMapper salesMapper;

    @Transactional
    @Override
    public SalesModel saveSale(final String cpf, final String vin) throws ClientNotFoundException, CarNotFoundException, ClientNotHaveRegisteredAddressException, CarAlreadySoldException {

        final var client = clientService.findByCpf(cpf);

        final var car = carService.findByVin(vin);

        if(client.isEmpty()) throw new ClientNotFoundException("There isn't a client with this CPF");

        if(car.isEmpty()) throw new CarNotFoundException("There isn't a car with this VIN");

        if(!client.get().getAddress().isAddressSearched())
            throw new ClientNotHaveRegisteredAddressException("The client needs to have a registered address");

        final var sale = salesMapper.toSalesModel(car.get(),client.get());

        try{
            return salesRepositoryPort.save(sale);
        }catch (Exception ex){
            throw new CarAlreadySoldException("The car with this VIN was already sold");
        }
    }

    @Override
    public Page<SalesModel> getSales(final LocalDate initialDate, final LocalDate finalDate, final Pageable pageable) {

        if(ObjectUtils.allNotNull(initialDate,finalDate))
            return salesRepositoryPort.findAll(betweenDates(initialDate,finalDate),pageable);

        return salesRepositoryPort.findAll(pageable);
    }

    @Override
    public Optional<SalesModel> findById(String id) {
        return salesRepositoryPort.findById(id);
    }

    @Override
    public void deleteSale(String id) throws SaleNotFoundException {
        if(salesRepositoryPort.findById(id).isEmpty())
            throw new SaleNotFoundException("There isn't a sale with this id");

        salesRepositoryPort.deleteById(id);
    }
}
