package integrated;

import com.example.api.dealership.adapter.dtos.car.CarDtoRequest;
import com.example.api.dealership.adapter.dtos.car.CarDtoUpdateRequest;
import com.example.api.dealership.adapter.service.car.impl.CarServiceImpl;
import com.example.api.dealership.core.domain.CarModel;
import com.example.api.dealership.core.exceptions.DuplicatedInfoException;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

 class CarControllerIT extends BaseIT{

    private static final String URL_WITH_VIN_PATH_PARAMETER = "/v1/dealership/cars/{vin}";
    private static final String URL = "/v1/dealership/cars";
    //Token valido até 05/07/2024
    private static final String TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY4ODY5MjUwMywiZXhwIjoxNzIwMjI4NTAzfQ.i0PPlujf0KGcpxzejFI6b6BeSM8i3yr27qYo-TdXAOw";
    private static final String EXPIRED_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY4OTAzMDE0MiwiZXhwIjoxNjg5MDMwMTQyfQ.z-_TEnUlIFZhXRb_vf-ZL7aG9kPa1rZ7dOOE2E5csxQ";

    @Autowired
    private CarServiceImpl carService;

    @DisplayName("Given a valid request to create a car then create it")
    @Test
     void givenValidRequestToCreateACarThenCreateIt(){
        final var car = createCarDtoRequest("12345678911");

        RestAssured.given()
                .header("Authorization", "Bearer " + TOKEN)
                .contentType(ContentType.JSON)
                .body(car)
                .when()
                .post(URL)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("data.id", notNullValue())
                .body("data.model", equalTo(car.model()))
                .body("data.modelYear", equalTo(car.modelYear()))
                .body("data.manufacturer",equalTo(car.manufacturer()))
                .body("data.vin", equalTo(car.vin()))
                .body("data.value", equalTo(car.value().floatValue()));
    }

    @DisplayName("Get a car by VIN with a valid request")
    @Test
     void givenValidRequestToGetACarThenGetIt() throws DuplicatedInfoException {
        final var car = createCar("11987654321");
        carService.save(car);

        RestAssured.given()
                .header("Authorization", "Bearer " + TOKEN)
                .pathParam("vin", "11987654321")
                .contentType(ContentType.JSON)
                .when()
                .get(URL_WITH_VIN_PATH_PARAMETER)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("data.id", notNullValue())
                .body("data.model", equalTo(car.getModel()))
                .body("data.modelYear", equalTo(car.getModelYear()))
                .body("data.manufacturer",equalTo(car.getManufacturer()))
                .body("data.vin", equalTo(car.getVin()))
                .body("data.value", equalTo(car.getValue().floatValue()));
    }

    @DisplayName("Given a VIN that is not registered, return 404")
    @Test
     void givenVINThatIsNotRegisteredReturnNotFound(){
        RestAssured.given()
                .header("Authorization", "Bearer " + TOKEN)
                .pathParam("vin", "1")
                .contentType(ContentType.JSON)
                .when()
                .get(URL_WITH_VIN_PATH_PARAMETER)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("Given a valid request get all cars")
    @Test
     void givenValidRequestGetAllCars(){
        RestAssured.given()
                .header("Authorization","Bearer " + TOKEN)
                .contentType(ContentType.JSON)
                .when()
                .get(URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("data",notNullValue())
                .body("data.content",notNullValue())
                .body("data.size",equalTo(10));
    }


    @DisplayName("Given a valid request to update a car, do it")
    @Test
     void givenValidRequestToUpdateCarDoIt() throws DuplicatedInfoException {
        final var model = createCar("22222222222");
        final var carDto = createCarDtoUpdateRequest("red",11111.11);

        carService.save(model);

        RestAssured.given()
                .header("Authorization","Bearer " + TOKEN)
                .contentType(ContentType.JSON)
                .body(carDto)
                .pathParam("vin","22222222222")
                .when()
                .put(URL_WITH_VIN_PATH_PARAMETER)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("data.color",equalTo(carDto.color()))
                .body("data.value",equalTo(carDto.value().floatValue()));
    }


    @DisplayName("Given a request with a invalid VIN to update a car, return 404")
    @Test
     void givenRequestWithInvalidVINToUpdateCarReturnNotFound(){
        final var carDto = createCarDtoUpdateRequest("red",111111.00);

        RestAssured.given()
                .header("Authorization","Bearer " + TOKEN)
                .contentType(ContentType.JSON)
                .body(carDto)
                .pathParam("vin","11987654321")
                .when()
                .put(URL_WITH_VIN_PATH_PARAMETER)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("Given a valid request to delete a car, do it")
    @Test
     void givenValidRequestToDeleteCarDoIt() throws DuplicatedInfoException {
        final var model = createCar("44444444444");
        carService.save(model);

        RestAssured.given()
                .header("Authorization","Bearer " + TOKEN)
                .contentType(ContentType.JSON)
                .pathParam("vin","44444444444")
                .when()
                .delete(URL_WITH_VIN_PATH_PARAMETER)
                .then()
                .statusCode(HttpStatus.OK.value());

    }

    @DisplayName("Given a request with a invalid vin to delete a car, return 404")
    @Test
     void givenRequestWithInvalidVINToDeleteCarReturnNotFound(){
        RestAssured.given()
                .header("Authorization","Bearer " + TOKEN)
                .contentType(ContentType.JSON)
                .pathParam("vin","0")
                .when()
                .delete(URL_WITH_VIN_PATH_PARAMETER)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("Given a request without a token, return 403")
    @Test
     void givenRequestWithoutTokenReturnForbidden(){
        RestAssured.given()
                .pathParam("vin", "12345678911")
                .contentType(ContentType.JSON)
                .when()
                .get(URL_WITH_VIN_PATH_PARAMETER)
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @DisplayName("Given a request with a invalid token, return 403")
    @Test
     void givenInvalidTokenReturnForbidden(){
        RestAssured.given()
                .header("Authorization", "Bearer " + "12345")
                .pathParam("vin", "12345678911")
                .contentType(ContentType.JSON)
                .when()
                .get(URL_WITH_VIN_PATH_PARAMETER)
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @DisplayName("Given a request with a expired token, return 403")
    @Test
     void givenExpiredTokenReturnForbidden(){
        RestAssured.given()
                .header("Authorization", "Bearer " + EXPIRED_TOKEN)
                .pathParam("vin", "12345678911")
                .contentType(ContentType.JSON)
                .when()
                .get(URL_WITH_VIN_PATH_PARAMETER)
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    private static CarDtoUpdateRequest createCarDtoUpdateRequest(String color, Double value){
        return CarDtoUpdateRequest.builder()
                .color(color)
                .value(value)
                .build();
    }

    private static CarDtoRequest createCarDtoRequest(String vin){
        return CarDtoRequest.builder()
                .color("Black")
                .manufacturer("Audi")
                .model("A3")
                .modelYear("2021")
                .vin(vin)
                .value(100000.0)
                .build();
    }

    private static CarModel createCar(String vin){
        return CarModel.builder()
                .registrationDate(LocalDateTime.now())
                .color("Black")
                .manufacturer("Audi")
                .model("A3")
                .modelYear("2021")
                .vin(vin)
                .value(100000.0)
                .build();
    }

}
