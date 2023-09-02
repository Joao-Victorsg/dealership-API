package com.example.api.dealership.adapter.entrypoint;

import com.example.api.dealership.adapter.dtos.Response;
import com.example.api.dealership.adapter.dtos.user.UserDtoRequest;
import com.example.api.dealership.adapter.mapper.UserMapper;
import com.example.api.dealership.adapter.service.user.UserService;
import com.example.api.dealership.core.domain.UserModel;
import com.example.api.dealership.core.exceptions.UsernameAlreadyUsedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;


    @Test
    @DisplayName("Given a valid username and password, save it in the database")
    void givenValidUsernameAndPasswordSaveItInTheDatabase() throws UsernameAlreadyUsedException {
        final var userDtoRequest = getUserDtoRequest();
        final var user = getUserModel();
        final var expectedResponse = ResponseEntity.created(URI.create("/v1/dealership/users/"))
                .body(Response.createResponse("User: " + user.getUsername() + " created with success"));


        when(userMapper.toUserModel(userDtoRequest)).thenReturn(user);
        when(userService.saveUser(user)).thenReturn(user);

        assertDoesNotThrow(() -> {
            final var response = userController.saveUser(userDtoRequest);
            assertEquals(expectedResponse.getStatusCode(),response.getStatusCode());
            assertEquals(expectedResponse.getBody().getData(),response.getBody().getData().toString());
        });
    }


    @Test
    @DisplayName("Given a username that already exists, return Bad Request")
    void givenUsernameThatAlreadyExistsReturnBadRequest() throws UsernameAlreadyUsedException {
        final var userDtoRequest = getUserDtoRequest();
        final var userModel = getUserModel();
        final var expectedResponse = ResponseEntity.badRequest()
                .body(Response.createResponse("This username already exists."));

        when(userMapper.toUserModel(userDtoRequest)).thenReturn(userModel);
        doThrow(UsernameAlreadyUsedException.class).when(userService).saveUser(userModel);

        assertThrows(UsernameAlreadyUsedException.class,() -> {
            final var response = userController.saveUser(userDtoRequest);
            assertEquals(expectedResponse.getStatusCode(),response.getStatusCode());
            assertEquals(expectedResponse.getBody().getData(),response.getBody().getData());
        });
    }

    private UserDtoRequest getUserDtoRequest(){
        return UserDtoRequest.builder()
                .username("username")
                .password("password")
                .build();
    }

    private UserModel getUserModel(){
        return UserModel.builder()
                .username("username")
                .password("password")
                .build();
    }

}
