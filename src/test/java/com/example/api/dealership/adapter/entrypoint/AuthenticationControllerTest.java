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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
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

        verify(authenticationService).authenticate(userDtoRequest);
        verifyNoMoreInteractions(authenticationService);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody().getData(),"token");
    }

    @Test
    @DisplayName("Given a invalid user and password, return UNAUTHORIZED")
    void givenInvalidUserAndPasswordThrowException(){
        final var userDtoRequest = UserDtoRequest.builder().build();

        when(authenticationService.authenticate(userDtoRequest))
                .thenThrow(new RuntimeException("This user is unauthorized"));

        final var response = authenticationController.authenticate(userDtoRequest);

        verify(authenticationService).authenticate(userDtoRequest);
        verifyNoMoreInteractions(authenticationService);

        assertEquals(HttpStatus.UNAUTHORIZED,response.getStatusCode());
    }



}