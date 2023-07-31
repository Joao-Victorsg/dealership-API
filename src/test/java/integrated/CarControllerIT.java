package integrated;

import com.example.api.dealership.adapter.dtos.car.CarDtoRequest;
import com.example.api.dealership.adapter.service.car.impl.CarServiceImpl;
import com.example.api.dealership.core.domain.CarModel;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CarControllerIT extends BaseIT{

    private static final String URL_WITH_VIN_PATH_PARAMETER = "/v1/dealership/cars/{vin}";
    private static final String URL = "/v1/dealership/cars";
    //Token valido até 05/07/2024
    private static final String TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY4ODY5MjUwMywiZXhwIjoxNzIwMjI4NTAzfQ.i0PPlujf0KGcpxzejFI6b6BeSM8i3yr27qYo-TdXAOw";
    private static final String EXPIRED_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY4OTAzMDE0MiwiZXhwIjoxNjg5MDMwMTQyfQ.z-_TEnUlIFZhXRb_vf-ZL7aG9kPa1rZ7dOOE2E5csxQ";

    @Autowired
    private CarServiceImpl carService;

    @DisplayName("Given a valid request to create a car then create it")
    @Test
    public void givenValidRequestToCreateACarThenCreateIt(){
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
                .body("data.carModel", equalTo(car.getCarModel()))
                .body("data.carModelYear", equalTo(car.getCarModelYear()))
                .body("data.carMake",equalTo(car.getCarMake()))
                .body("data.carVin", equalTo(car.getCarVin()))
                .body("data.carValue", equalTo(car.getCarValue().floatValue()));
    }

    @DisplayName("Get a car by VIN with a valid request")
    @Test
    public void givenValidRequestToGetACarThenGetIt(){
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
                .body("data.carModel", equalTo(car.getCarModel()))
                .body("data.carModelYear", equalTo(car.getCarModelYear()))
                .body("data.carMake",equalTo(car.getCarMake()))
                .body("data.carVin", equalTo(car.getCarVin()))
                .body("data.carValue", equalTo(car.getCarValue().floatValue()));
    }

    @DisplayName("Given a VIN that is not registered, return 404")
    @Test
    public void givenVINThatIsNotRegisteredReturnNotFound(){
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
    public void givenValidRequestGetAllCars(){
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
    public void givenValidRequestToUpdateCarDoIt(){
        final var carModel = createCar("22222222222");
        final var carDto = createCarDtoRequest("22222222222");
        carDto.setCarColor("Red");
        carDto.setCarValue(11111.11);

        carService.save(carModel);

        RestAssured.given()
                .header("Authorization","Bearer " + TOKEN)
                .contentType(ContentType.JSON)
                .body(carDto)
                .pathParam("vin","22222222222")
                .when()
                .put(URL_WITH_VIN_PATH_PARAMETER)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("data.carColor",equalTo(carDto.getCarColor()))
                .body("data.carValue",equalTo(carDto.getCarValue().floatValue()));
    }

    @DisplayName("Given a request with a invalid VIN to update a car, return 404")
    @Test
    public void givenRequestWithInvalidVINToUpdateCarReturnNotFound(){
        final var carModel = createCar("33333333333");
        final var carDto = createCarDtoRequest("33333333333");

        carService.save(carModel);

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
    public void givenValidRequestToDeleteCarDoIt(){
        final var carModel = createCar("44444444444");
        carService.save(carModel);

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
    public void givenRequestWithInvalidVINToDeleteCarReturnNotFound(){
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
    public void givenRequestWithoutTokenReturnForbidden(){
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
    public void givenInvalidTokenReturnForbidden(){
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
    public void givenExpiredTokenReturnForbidden(){
        RestAssured.given()
                .header("Authorization", "Bearer " + EXPIRED_TOKEN)
                .pathParam("vin", "12345678911")
                .contentType(ContentType.JSON)
                .when()
                .get(URL_WITH_VIN_PATH_PARAMETER)
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    private static CarDtoRequest createCarDtoRequest(String vin){
        return CarDtoRequest.builder()
                .carColor("Black")
                .carMake("Audi")
                .carModel("A3")
                .carModelYear("2021")
                .carVin(vin)
                .carValue(100000.0)
                .build();
    }

    private static CarModel createCar(String vin){
        return CarModel.builder()
                .carRegistrationDate(LocalDateTime.now())
                .carColor("Black")
                .carMake("Audi")
                .carModel("A3")
                .carModelYear("2021")
                .carVin(vin)
                .carValue(100000.0)
                .build();
    }

}
