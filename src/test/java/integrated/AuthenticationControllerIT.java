package integrated;


import com.example.api.dealership.adapter.dtos.user.UserDtoRequest;
import com.example.api.dealership.adapter.service.user.impl.UserServiceImpl;
import com.example.api.dealership.core.domain.UserModel;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;

public class AuthenticationControllerIT extends BaseIT{

    private final static String AUTH_URL = "/v1/dealership/auths";

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;



    @DisplayName("Given a valid user return a token")
    @Test
    void givenValidUserReturnToken(){
        final var userDto = createUserDto();
        final var userModel = createUserModel();
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
                .body("data",nullValue())
                .body("errors", equalTo("This user is unauthorized"));
    }

    private UserDtoRequest createUserDto(){
        return UserDtoRequest.builder()
                .username("teste")
                .password("teste")
                .build();
    }

    private UserModel createUserModel(){
        return UserModel.builder()
                .username("teste")
                .password(passwordEncoder.encode("teste"))
                .isAdmin(true)
                .build();
    }

}
