package integrated;


import com.example.api.dealership.adapter.dtos.user.UserDtoRequest;
import com.example.api.dealership.adapter.service.user.impl.UserServiceImpl;
import com.example.api.dealership.core.domain.UserModel;
import com.example.api.dealership.core.exceptions.UsernameAlreadyUsedException;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

class AuthenticationControllerIT extends BaseIT{

    private final static String AUTH_URL = "v1/dealership/auths";

    @Autowired
    private UserServiceImpl userService;

    @DisplayName("Given a valid user return a token")
    @Test
    void givenValidUserReturnToken() throws UsernameAlreadyUsedException {
        final var userDto = createUserDto();
        final var userModel = createUserModel(userDto);
        userService.saveUser(userModel);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(userDto)
                .when()
                .post(AUTH_URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("data", notNullValue());
    }

    @DisplayName("Given a invalid user return Not Found")
    @Test
    void givenInvalidUserReturnNotFound(){
        final var userDto = createUserDto();

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(userDto)
                .when()
                .post(AUTH_URL)
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .body("data.timestamp",notNullValue())
                .body("data.details",equalTo("Bad credentials"));
    }

    private UserDtoRequest createUserDto(){
        return UserDtoRequest.builder()
                .username("teste")
                .password("teste")
                .build();
    }

    private UserModel createUserModel(UserDtoRequest user){
        return UserModel.builder()
                .username(user.username())
                .password(user.password())
                .build();
    }

}