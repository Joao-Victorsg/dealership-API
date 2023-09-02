package integrated;

import com.example.api.dealership.adapter.dtos.sales.SalesDtoRequest;
import com.example.api.dealership.adapter.service.car.impl.CarServiceImpl;
import com.example.api.dealership.adapter.service.client.impl.ClientServiceImpl;
import com.example.api.dealership.adapter.service.sales.impl.SalesServiceImpl;
import com.example.api.dealership.core.domain.AddressModel;
import com.example.api.dealership.core.domain.CarModel;
import com.example.api.dealership.core.domain.ClientModel;
import com.example.api.dealership.core.exceptions.CarAlreadySoldException;
import com.example.api.dealership.core.exceptions.CarNotFoundException;
import com.example.api.dealership.core.exceptions.ClientNotFoundException;
import com.example.api.dealership.core.exceptions.ClientNotHaveRegisteredAddressException;
import com.example.api.dealership.core.exceptions.DuplicatedInfoException;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static integrated.wiremock.MockServer.mockGetAddressByPostCode;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

class SalesControllerIT extends BaseIT {

    private static final String URL_WITH_VIN_PATH_PARAMETER = "/v1/dealership/sales/{id}";
    private static final String URL = "/v1/dealership/sales";
    //Token valido até 05/07/2024
    private static final String TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY4ODY5MjUwMywiZXhwIjoxNzIwMjI4NTAzfQ.i0PPlujf0KGcpxzejFI6b6BeSM8i3yr27qYo-TdXAOw";
    private static final String EXPIRED_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY4OTAzMDE0MiwiZXhwIjoxNjg5MDMwMTQyfQ.z-_TEnUlIFZhXRb_vf-ZL7aG9kPa1rZ7dOOE2E5csxQ";

    @Autowired
    private CarServiceImpl carService;

    @Autowired
    private ClientServiceImpl clientService;

    @Autowired
    private SalesServiceImpl salesService;

    @DisplayName("Given a valid request to create a sales then create it")
    @Test
    void givenValidRequestToCreateASalesThenCreateIt() throws DuplicatedInfoException {

        final var car = createCar("222222222");
        final var client = createClient("987654321");
        final var sales = createSalesDtoRequest(client.getCpf(), car.getVin());
        carService.save(car);
        clientService.saveClient(client);

        RestAssured.given()
                .header("Authorization", "Bearer " + TOKEN)
                .contentType(ContentType.JSON)
                .body(sales)
                .when()
                .post(URL)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("data.id", notNullValue())
                .body("data.registrationDate", notNullValue())
                .body("data.car", notNullValue())
                .body("data.client", notNullValue());
    }

    @DisplayName("Get a sales by ID with a valid request")
    @Test
    void givenValidRequestToGetASaleThenGetIt() throws DuplicatedInfoException, CarAlreadySoldException, ClientNotFoundException, ClientNotHaveRegisteredAddressException, CarNotFoundException {
        final var car = createCar("11111111111");
        final var client = createClient("12345678911");
        mockGetAddressByPostCode(client.getAddress().getPostCode());

        carService.save(car);
        clientService.saveClient(client);
        final var salesModel = salesService.saveSale(client.getCpf(), car.getVin());

        RestAssured.given()
                .header("Authorization", "Bearer " + TOKEN)
                .pathParam("id", salesModel.getId())
                .contentType(ContentType.JSON)
                .when()
                .get(URL_WITH_VIN_PATH_PARAMETER)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("data.id", notNullValue())
                .body("data.registrationDate", notNullValue())
                .body("data.car.model", equalTo(car.getModel()))
                .body("data.car.modelYear", equalTo(car.getModelYear()))
                .body("data.car.manufacturer", equalTo(car.getManufacturer()))
                .body("data.car.vin", equalTo(car.getVin()))
                .body("data.car.value", equalTo(car.getValue().floatValue()))
                .body("data.car.registrationDate", notNullValue())
                .body("data.client.name", equalTo(client.getName()))
                .body("data.client.cpf", equalTo(client.getCpf()))
                .body("data.client.address", notNullValue())
                .body("data.client.registrationDate", notNullValue());
    }

    @DisplayName("Given a sale that is not registered, return 404")
    @Test
    void givenVINThatIsNotRegisteredReturnNotFound() {
        RestAssured.given()
                .header("Authorization", "Bearer " + TOKEN)
                .pathParam("id", "1")
                .contentType(ContentType.JSON)
                .when()
                .get(URL_WITH_VIN_PATH_PARAMETER)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("Given a valid request get all sales")
    @Test
    void givenValidRequestGetAllCars() {
        RestAssured.given()
                .header("Authorization", "Bearer " + TOKEN)
                .contentType(ContentType.JSON)
                .when()
                .get(URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("data", notNullValue())
                .body("data.content", notNullValue())
                .body("data.size", equalTo(10));
    }

    @DisplayName("Given a valid request to delete a sales, do it")
    @Test
    void givenValidRequestToDeleteSalesDoIt() throws DuplicatedInfoException, CarAlreadySoldException, ClientNotFoundException, ClientNotHaveRegisteredAddressException, CarNotFoundException {
        final var carModel = createCar("44444444444");
        final var clientModel = createClient("11112222333");
        mockGetAddressByPostCode(clientModel.getAddress().getPostCode());

        carService.save(carModel);
        clientService.saveClient(clientModel);
        final var salesResponse = salesService.saveSale(clientModel.getCpf(), carModel.getVin());

        RestAssured.given()
                .header("Authorization", "Bearer " + TOKEN)
                .contentType(ContentType.JSON)
                .pathParam("id", salesResponse.getId())
                .when()
                .delete(URL_WITH_VIN_PATH_PARAMETER)
                .then()
                .statusCode(HttpStatus.OK.value());

    }

    @DisplayName("Given a request with a invalid id to delete a sale, return 404")
    @Test
    void givenRequestWithInvalidVINToDeleteCarReturnNotFound() {
        RestAssured.given()
                .header("Authorization", "Bearer " + TOKEN)
                .contentType(ContentType.JSON)
                .pathParam("id", "0")
                .when()
                .delete(URL_WITH_VIN_PATH_PARAMETER)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("Given a request without a token, return 403")
    @Test
    void givenRequestWithoutTokenReturnForbidden() {
        RestAssured.given()
                .pathParam("id", "12345678911")
                .contentType(ContentType.JSON)
                .when()
                .get(URL_WITH_VIN_PATH_PARAMETER)
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @DisplayName("Given a request with a invalid token, return 403")
    @Test
    void givenInvalidTokenReturnForbidden() {
        RestAssured.given()
                .header("Authorization", "Bearer " + "12345")
                .pathParam("id", "12345678911")
                .contentType(ContentType.JSON)
                .when()
                .get(URL_WITH_VIN_PATH_PARAMETER)
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @DisplayName("Given a request with a expired token, return 403")
    @Test
    void givenExpiredTokenReturnForbidden() {
        RestAssured.given()
                .header("Authorization", "Bearer " + EXPIRED_TOKEN)
                .pathParam("id", "12345678911")
                .contentType(ContentType.JSON)
                .when()
                .get(URL_WITH_VIN_PATH_PARAMETER)
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    private static CarModel createCar(String vin) {
        return CarModel.builder()
                .registrationDate(LocalDateTime.now(ZoneId.of("UTC")))
                .color("Black")
                .manufacturer("Audi")
                .model("A3")
                .modelYear("2021")
                .vin(vin)
                .value(100000.0)
                .build();
    }

    private static SalesDtoRequest createSalesDtoRequest(String cpf, String vin) {
        return SalesDtoRequest.builder()
                .cpf(cpf)
                .vin(vin)
                .build();
    }

    private static ClientModel createClient(String cpf) {
        return ClientModel.builder()
                .registrationDate(LocalDateTime.now(ZoneId.of("UTC")))
                .name("teste-integrado")
                .cpf(cpf)
                .address(AddressModel.builder()
                        .streetNumber("321")
                        .postCode("36888-888")
                        .build())
                .build();
    }

}