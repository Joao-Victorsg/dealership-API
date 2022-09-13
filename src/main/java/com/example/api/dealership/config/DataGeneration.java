package com.example.api.dealership.config;

import com.example.api.dealership.core.adapter.car.impl.CarRepositoryAdapterImpl;
import com.example.api.dealership.core.adapter.client.impl.ClientRepositoryAdapterImpl;
import com.example.api.dealership.core.adapter.sales.SalesRepositoryAdapter;
import com.example.api.dealership.core.domain.CarModel;
import com.example.api.dealership.core.domain.ClientModel;
import com.example.api.dealership.core.dtos.car.CarDtoRequest;
import com.example.api.dealership.core.dtos.client.ClientDtoRequest;
import com.example.api.dealership.core.mapper.CarMapper;
import com.example.api.dealership.core.mapper.ClientMapper;
import com.example.api.dealership.core.mapper.SalesMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DataGeneration {

    private final ClientRepositoryAdapterImpl clientRepositoryAdapter;
    private final ClientMapper clientMapper;
    private final CarRepositoryAdapterImpl carRepositoryAdapter;
    private final CarMapper carMapper;
    private final SalesRepositoryAdapter salesRepositoryAdapter;
    private final SalesMapper salesMapper;

    @Bean
    public void saveMockingData(){
        var clients = saveClientModel(generateClientData());
        var cars = saveCarModel(generateCarData());
        saveSales(clients,cars);
    }

    private void saveSales(List<ClientModel> clients,List<CarModel> cars){
        for(int i=0;i < 100;i++){
            Random random = ThreadLocalRandom.current();

            var randomIntClient = (random.nextInt(clients.size() - 1 + 1));
            var randomIntCar = (random.nextInt(cars.size() - 1 + 1));

            var client = clients.get(randomIntClient);
            var car = cars.get(randomIntCar);

            cars.remove(randomIntCar);

            try {
                salesRepositoryAdapter.saveSale(salesMapper.toSalesModel(car, client));
            }catch (Exception ex) {
                log.error("Deu ruim no save do sale",ex);
            }
        }
    }

    private List<CarModel> saveCarModel(List<CarDtoRequest> cars){
        var carsModel = cars.stream().map(carDtoRequest -> carMapper.toCarModel(carDtoRequest)).collect(Collectors.toList());

        var savedCarsModel = new ArrayList<CarModel>();

        try {
            carsModel.forEach(car -> savedCarsModel.add(carRepositoryAdapter.save(car)));

        }catch (Exception ex){
            log.error("Deu ruim no save do carro",ex);
        }
        return savedCarsModel;
    }

    private List<ClientModel> saveClientModel(List<ClientDtoRequest> clients){
        var clientsModel = clients.stream().map(clientDtoRequest -> clientMapper.toClientModel(clientDtoRequest)).collect(Collectors.toList());

        var clientsModelCpf = new ArrayList<ClientModel>();

        try {
            clientsModel.forEach(client -> clientsModelCpf.add(clientRepositoryAdapter.saveClient(client)));
            return clientsModelCpf;
        }catch (Exception ex){
            log.error("Deu ruim no save do client",ex);
        }
        return clientsModelCpf;
    }

    private List<ClientDtoRequest> generateClientData() {

            Faker faker = new Faker(new Locale("pt-BR"));

            var clientDtoRequests = generateClientAddressData(faker);

            List<String> clientsCPF = faker.collection(() -> faker.cpf().valid(false)).len(100).generate();

            var clientsRequests = new ArrayList<ClientDtoRequest>();

            for(int i=0; i < 100; i++){
                Random random = ThreadLocalRandom.current();

                var clientDtoRequest = clientDtoRequests.get((random.nextInt(clientDtoRequests.size() - 1 + 1)+1));

                clientDtoRequest.setName(faker.name().name());
                clientDtoRequest.setCpf(clientsCPF.get(i));
                clientsRequests.add(clientDtoRequest);
            }

            return clientsRequests;
    }

    private List<ClientDtoRequest> generateClientAddressData(Faker faker){
        try {

            String jsonText = AbstractReader.readJson("./static/MOCK_ADDRESS_DATA.json");

            var type = new TypeToken<List<ClientDtoRequest>>() {}.getType();

            List<ClientDtoRequest> clientDtoRequests = new Gson().fromJson(jsonText, type);

            clientDtoRequests.forEach(address -> address.setStreetNumber(faker.address().streetAddressNumber()));

            return clientDtoRequests;

        }catch (IOException ex){
            log.error("Erro na leitura do arquivo",ex);
            return List.of();
        }
    }

    private List<CarDtoRequest> generateCarData(){
        try {
            String jsonText = AbstractReader.readJson("./static/MOCK_VEIC_DATA.json");
            var type = new TypeToken<List<CarDtoRequest>>(){}.getType();
            List<CarDtoRequest> cars = new Gson().fromJson(jsonText,type);
            // Perguntar em relação ao stream, pq n tá funcionando
            cars.forEach(car -> car.setCarValue(generateRandomDoubleValueFromRange(0.00,400000.00)));
            return cars;
        }catch (IOException ex){
            log.error("Erro na leitura do arquivo",ex);
            return List.of();
        }
    }

    private Double generateRandomDoubleValueFromRange(Double min, Double max) {
        Random random = new Random();
        Double randomDouble = min + (max - min) * random.nextDouble();
        return randomDouble;
    }

}
