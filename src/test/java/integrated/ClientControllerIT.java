package integrated;

import com.example.api.dealership.adapter.dtos.client.ClientDtoRequest;
import com.example.api.dealership.adapter.service.client.impl.ClientServiceImpl;
import com.example.api.dealership.core.domain.AddressModel;
import com.example.api.dealership.core.domain.ClientModel;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

import static integrated.wiremock.MockServer.mockGetAddressByPostCode;
import static integrated.wiremock.MockServer.mockGetAddressByPostCodeWithServerError;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

@RequiredArgsConstructor
public class ClientControllerIT extends BaseIT{
    private static final String URL_WITH_CPF_PATH_PARAMETER = "/v1/dealership/clients/{cpf}";
    private static final String URL = "/v1/dealership/clients";
    //Token valido até 05/07/2024
    private static final String TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY4ODY5MjUwMywiZXhwIjoxNzIwMjI4NTAzfQ.i0PPlujf0KGcpxzejFI6b6BeSM8i3yr27qYo-TdXAOw";
    private static final String EXPIRED_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY4OTAzMDE0MiwiZXhwIjoxNjg5MDMwMTQyfQ.z-_TEnUlIFZhXRb_vf-ZL7aG9kPa1rZ7dOOE2E5csxQ";

    @Autowired
    private ClientServiceImpl clientService;

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @DisplayName("Given a valid request to create a client then create it")
    @Test
    public void givenValidRequestToCreateAClienteThenCreateIt(){
        final var client = createClientDtoRequest("12345678911");
        mockGetAddressByPostCode("39999-999");

       RestAssured.given()
                   .header("Authorization", "Bearer " + TOKEN)
                   .contentType(ContentType.JSON)
                   .body(client)
               .when()
                   .post(URL)
               .then()
                   .statusCode(HttpStatus.CREATED.value())
                   .body("data.name", equalTo(client.getName()))
                   .body("data.cpf", equalTo(client.getCpf()))
                   .body("data.address.cep", equalTo("39999-999"))
                   .body("data.address.logradouro",notNullValue())
                   .body("data.address.uf", notNullValue())
                   .body("data.address.localidade", notNullValue());
    }

    @DisplayName("Should create the client even if the via cep api returns an exception")
    @Test
    public void shouldCreateClientEvenIfViaCepApiReturnsAnException(){
        final var client = createClientDtoRequest("12345678912");
        mockGetAddressByPostCodeWithServerError("39999-999");

        RestAssured.given()
                .header("Authorization", "Bearer " + TOKEN)
                .contentType(ContentType.JSON)
                .body(client)
                .when()
                .post(URL)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("data.name", equalTo(client.getName()))
                .body("data.cpf", equalTo(client.getCpf()))
                .body("data.address.cep", equalTo("39999-999"))
                .body("data.address.addressSearched",equalTo(false));
    }

    @DisplayName("Should create the client even with the circuit break from the via cep api in open state")
    @Test
    public void shouldCreateClientEvenWithTheCircuitBreakerInOpenState(){
        final var client = createClientDtoRequest("12345678913");
        circuitBreakerRegistry.circuitBreaker("SearchAddressGatewaybyPostCode").transitionToOpenState();

        RestAssured.given()
                .header("Authorization", "Bearer " + TOKEN)
                .contentType(ContentType.JSON)
                .body(client)
                .when()
                .post(URL)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("data.name", equalTo(client.getName()))
                .body("data.cpf", equalTo(client.getCpf()))
                .body("data.address.cep", equalTo("39999-999"))
                .body("data.address.addressSearched",equalTo(false));

        circuitBreakerRegistry.circuitBreaker("SearchAddressGatewaybyPostCode").transitionToClosedState();
    }

    @DisplayName("Get a client by CPF with a valid request")
    @Test
    public void givenValidRequestToGetAClientThenGetIt(){
        final var client = createClient("11987654321");
        clientService.saveClient(client);

        RestAssured.given()
                    .header("Authorization", "Bearer " + TOKEN)
                    .pathParam("cpf", "11987654321")
                    .contentType(ContentType.JSON)
                .when()
                    .get(URL_WITH_CPF_PATH_PARAMETER)
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.name", equalTo(client.getName()))
                    .body("data.cpf", equalTo(client.getCpf()))
                    .body("data.address.cep", equalTo(client.getAddress().getPostCode()))
                    .body("data.address.logradouro", equalTo(client.getAddress().getStreetName()))
                    .body("data.address.uf", equalTo(client.getAddress().getStateAbbreviation()))
                    .body("data.address.localidade", equalTo(client.getAddress().getCity()));
    }

    @DisplayName("Given a CPF that is not registered, return 404")
    @Test
    public void givenCPFThatIsNotRegisteredReturnNotFound(){
        RestAssured.given()
                    .header("Authorization", "Bearer " + TOKEN)
                    .pathParam("cpf", "1")
                    .contentType(ContentType.JSON)
                .when()
                    .get(URL_WITH_CPF_PATH_PARAMETER)
                .then()
                    .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("Given a valid request get all clients")
    @Test
    public void givenValidRequestGetAllClients(){
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

    @DisplayName("Given a valid request to update a client, do it")
    @Test
    public void givenValidRequestToUpdateClientDoIt(){
        final var clientModel = createClient("22222222222");
        final var clientDto = createClientDtoRequest("22222222222");
        clientDto.setPostCode("39999-999");
        mockGetAddressByPostCode("39999-999");

        clientService.saveClient(clientModel);

        RestAssured.given()
                    .header("Authorization","Bearer " + TOKEN)
                    .contentType(ContentType.JSON)
                    .body(clientDto)
                    .pathParam("cpf","22222222222")
                .when()
                    .put(URL_WITH_CPF_PATH_PARAMETER)
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.name",equalTo("teste-integrado"))
                    .body("data.address.localidade",equalTo("Test"))
                    .body("data.address.uf",equalTo("TT"))
                    .body("data.address.logradouro",equalTo("Rua teste"));
    }

    @DisplayName("Given a request with a invalid cpf to update a client, return 404")
    @Test
    public void givenRequestWithInvalidCpfToUpdateClientReturnNotFound(){
        final var clientModel = createClient("33333333333");
        final var clientDto = createClientDtoRequest("33333333333");
        clientDto.setPostCode("39999-999");
        mockGetAddressByPostCode("39999-999");

        clientService.saveClient(clientModel);

        RestAssured.given()
                .header("Authorization","Bearer " + TOKEN)
                .contentType(ContentType.JSON)
                .body(clientDto)
                .pathParam("cpf","11987654321")
                .when()
                .put(URL_WITH_CPF_PATH_PARAMETER)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("Given a valid request to delete a client, do it")
    @Test
    public void givenValidRequestToDeleteClientDoIt(){
        final var clientModel = createClient("44444444444");
        clientService.saveClient(clientModel);

        RestAssured.given()
                    .header("Authorization","Bearer " + TOKEN)
                    .contentType(ContentType.JSON)
                    .pathParam("cpf","12345678911")
                .when()
                    .delete(URL_WITH_CPF_PATH_PARAMETER)
                .then()
                    .statusCode(HttpStatus.OK.value());

    }

    @DisplayName("Given a request with a invalid cpf to delete a client, return 404")
    @Test
    public void givenRequestWithInvalidCpfToDeleteClientReturnNotFound(){
        RestAssured.given()
                    .header("Authorization","Bearer " + TOKEN)
                    .contentType(ContentType.JSON)
                    .pathParam("cpf","0")
                .when()
                    .delete(URL_WITH_CPF_PATH_PARAMETER)
                .then()
                    .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("Given a request without a token, return 403")
    @Test
    public void givenRequestWithoutTokenReturnForbidden(){
        RestAssured.given()
                    .pathParam("cpf", "12345678911")
                    .contentType(ContentType.JSON)
                .when()
                    .get(URL_WITH_CPF_PATH_PARAMETER)
                .then()
                    .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @DisplayName("Given a request with a invalid token, return 403")
    @Test
    public void givenInvalidTokenReturnForbidden(){
        RestAssured.given()
                    .header("Authorization", "Bearer " + "12345")
                    .pathParam("cpf", "12345678911")
                    .contentType(ContentType.JSON)
                .when()
                    .get(URL_WITH_CPF_PATH_PARAMETER)
                .then()
                    .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @DisplayName("Given a request with a expired token, return 403")
    @Test
    public void givenExpiredTokenReturnForbidden(){
        RestAssured.given()
                    .header("Authorization", "Bearer " + EXPIRED_TOKEN)
                    .pathParam("cpf", "12345678911")
                    .contentType(ContentType.JSON)
                .when()
                    .get(URL_WITH_CPF_PATH_PARAMETER)
                .then()
                    .statusCode(HttpStatus.FORBIDDEN.value());
    }

    private static ClientDtoRequest createClientDtoRequest(String cpf){
        return ClientDtoRequest.builder()
                .name("teste-integrado")
                .cpf(cpf)
                .postCode("39999-999")
                .streetNumber("321")
                .build();
    }

    private static ClientModel createClient(String cpf){
        return ClientModel.builder()
                .registrationDate(LocalDateTime.now())
                .name("teste-integrado")
                .cpf(cpf)
                .address(AddressModel.builder()
                        .city("Muriaé")
                        .stateAbbreviation("MG")
                        .streetName("Rua Padre Arnaldo")
                        .streetNumber("321")
                        .postCode("36881-041")
                        .isAddressSearched(true)
                        .build())
                .build();
    }

}
