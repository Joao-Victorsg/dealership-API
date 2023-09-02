package com.example.api.dealership.config.data_generation;

import com.example.api.dealership.adapter.dtos.car.CarDtoRequest;
import com.example.api.dealership.adapter.dtos.client.ClientDtoRequest;
import com.example.api.dealership.adapter.dtos.client.address.AddressDtoRequest;
import com.example.api.dealership.adapter.dtos.client.address.AddressDtoResponse;
import com.example.api.dealership.adapter.mapper.CarMapper;
import com.example.api.dealership.adapter.mapper.ClientMapper;
import com.example.api.dealership.adapter.output.repository.port.CarRepositoryPort;
import com.example.api.dealership.adapter.output.repository.port.ClientRepositoryPort;
import com.example.api.dealership.adapter.service.sales.SalesService;
import com.example.api.dealership.adapter.service.user.UserService;
import com.example.api.dealership.core.domain.CarModel;
import com.example.api.dealership.core.domain.ClientModel;
import com.example.api.dealership.core.domain.UserModel;
import com.example.api.dealership.core.exceptions.UsernameAlreadyUsedException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Configuration
@Profile("!prod")
@RequiredArgsConstructor
public class DataGeneration {

    private final ClientRepositoryPort clientRepositoryPort;
    private final CarRepositoryPort carRepositoryPort;
    private final CarMapper carMapper;
    private final SalesService salesService;
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Bean
    public void saveMockingData() throws UsernameAlreadyUsedException {
        final var clients = saveClientModel(generateClientData());
        final var cars = saveCarModel(generateCarData());
        saveSales(clients,cars);
        saveUser(generateUserData());
    }

    private void saveSales(List<ClientModel> clients,List<CarModel> cars){
        for(int i=0;i < 100;i++){
            Random random = ThreadLocalRandom.current();

            final var randomIntClient = (random.nextInt(clients.size() - 1 + 1));
            final var randomIntCar = (random.nextInt(cars.size() - 1 + 1));

            final var client = clients.get(randomIntClient);
            final var car = cars.get(randomIntCar);

            cars.remove(randomIntCar);
            clients.remove(randomIntClient);

            try {
                salesService.saveSale(client.getCpf(),car.getVin());
            }catch (Exception ex) {
                log.error("Deu ruim no save do sale",ex);
            }
        }
    }

    private List<CarModel> saveCarModel(List<CarDtoRequest> cars){
        final var carsModel = cars.stream()
                .map(carMapper::toCarModel)
                .toList();
        try {
            return carRepositoryPort.saveAll(carsModel);
        }catch (Exception ex){
            log.error("Deu ruim no save do carro",ex);
            return List.of();
        }
    }

    private List<ClientModel> saveClientModel(List<ClientDtoRequest> clients){
        final var clientsModel = clients.stream()
                .map(ClientMapper::toClientModel)
                .toList();

        final var clientAddressDtoResponse = generateClientAddressData();

        Random random = ThreadLocalRandom.current();

        clientsModel.forEach(client ->
                BeanUtils.copyProperties(clientAddressDtoResponse.get(
                                (random.nextInt(clientsModel.size() - 1 + 1) + 1)),
                        client.getAddress()));

        try {
            return clientRepositoryPort.saveAll(clientsModel);

        }catch (Exception ex){
            log.error("Deu ruim no save do client",ex);
            return List.of();
        }
    }

    private List<ClientDtoRequest> generateClientData() {

            final var  faker = new Faker(new Locale("pt-BR"));

            final var clientsCPF = faker.collection(() -> faker.cpf().valid(false)).len(100).generate();

            final var clientsRequests = new ArrayList<ClientDtoRequest>();

            for(int i=0; i < 100; i++){
                final var clientDtoRequest = ClientDtoRequest.builder()
                                .name(faker.name().name())
                                .cpf(clientsCPF.get(i))
                                .address(AddressDtoRequest.createAddress("361","111111111"))
                                .build();

                clientsRequests.add(clientDtoRequest);
            }

            return clientsRequests;
    }


    private static List<AddressDtoResponse> generateClientAddressData(){
        try {
            final var jsonText = AbstractReader.readJson("./static/MOCK_ADDRESS_DATA.json");
            final var type = new TypeToken<List<AddressDtoResponse>>(){}.getType();

            return new Gson().fromJson(jsonText,type);
        } catch (IOException ex) {
            log.error("Erro na leitura do arquivo",ex);
            return List.of();
        }
    }


    private List<CarDtoRequest> generateCarData(){
        try {
           final var jsonText = AbstractReader.readJson("./static/MOCK_VEIC_DATA.json");
           final var type = new TypeToken<List<CarDtoRequest>>(){}.getType();
           final List<CarDtoRequest> carsWithoutValue = new Gson().fromJson(jsonText,type);

           return carsWithoutValue.stream()
                    .map(this::buildCarDtoRequestWithValue)
                    .toList();
        }catch (IOException ex){
            log.error("Erro na leitura do arquivo",ex);
            return List.of();
        }
    }

    private CarDtoRequest buildCarDtoRequestWithValue(CarDtoRequest car){
        return CarDtoRequest.builder()
                .model(car.getModel())
                .modelYear(car.getModelYear())
                .manufacturer(car.getManufacturer())
                .color(car.getColor())
                .vin(car.getVin())
                .value(generateRandomDoubleValueFromRange(0.00,400000.00))
                .build();
    }

    private Double generateRandomDoubleValueFromRange(Double min, Double max) {
        Random random = new Random();
        Double randomDouble = min + (max - min) * random.nextDouble();
        return randomDouble;
    }

    private void saveUser(UserModel user) throws UsernameAlreadyUsedException {
        userService.saveUser(user);
    }

    private UserModel generateUserData() {
        return UserModel.builder()
                .username("admin")
                .password("123456")
                .isAdmin(true)
                .build();
    }

}
