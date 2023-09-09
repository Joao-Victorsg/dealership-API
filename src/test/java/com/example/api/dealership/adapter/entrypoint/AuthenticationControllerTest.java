package com.example.api.dealership.adapter.entrypoint;

import com.example.api.dealership.adapter.dtos.user.UserDtoRequest;
import com.example.api.dealership.adapter.service.security.AuthenticationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @InjectMocks
    private AuthenticationController authenticationController;

    @Mock
    private AuthenticationService authenticationService;


    @Test
    @DisplayName("Given a user and password, authenticate it and send back a token")
    void givenUserAndPasswordAuthenticateAndReturnToken(){
        final var userDtoRequest = UserDtoRequest.builder().build();

        when(authenticationService.authenticate(userDtoRequest)).thenReturn("token");

        final var response = authenticationController.authenticate(userDtoRequest);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("token",response.getBody().getData());
    }

    @Test
    @DisplayName("Given a invalid user and password, return UNAUTHORIZED")
    void givenInvalidUserAndPasswordThrowException(){
        final var userDtoRequest = UserDtoRequest.builder().build();
        final var expectedResponse = "This user is unauthorized";

        when(authenticationService.authenticate(userDtoRequest))
                .thenThrow(new RuntimeException("This user is unauthorized"));

        assertThrows(RuntimeException.class,
                () -> {
                    final var response = authenticationController.authenticate(userDtoRequest);
                    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
                    assertEquals(expectedResponse, response.getBody().getData());
                });
    }
}
