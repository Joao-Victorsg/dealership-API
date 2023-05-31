package com.example.api.dealership.adapter.entrypoint;

import com.example.api.dealership.adapter.dtos.user.UserDtoRequest;
import com.example.api.dealership.adapter.mapper.UserMapper;
import com.example.api.dealership.adapter.service.user.UserService;
import com.example.api.dealership.core.domain.UserModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;


    @Test
    @DisplayName("Given a valid username and password, save it in the database")
    void givenValidUsernameAndPasswordSaveItInTheDatabase(){
        final var userDtoRequest = getUserDtoRequest();
        final var userModel = getUserModel();
        final var encodedPassword = "encodedPassword";
        final var userModelWithEncodedPassword = getUserModel();
        userModelWithEncodedPassword.setPassword(encodedPassword);
        final var expectedResponse = "User: username created with success";

        when(userService.findByUsername(userDtoRequest.getUsername())).thenReturn(Optional.empty());
        when(userMapper.toUserModel(userDtoRequest)).thenReturn(getUserModel());
        when(passwordEncoder.encode(userModel.getPassword())).thenReturn(encodedPassword);
        when(userService.saveUser(userModelWithEncodedPassword)).thenReturn(userModelWithEncodedPassword);

        final var response = userController.saveUser(userDtoRequest);

        verify(userService).findByUsername(userDtoRequest.getUsername());
        verify(userMapper).toUserModel(userDtoRequest);
        verify(passwordEncoder).encode(userModel.getPassword());
        verify(userService).saveUser(userModelWithEncodedPassword);

        verifyNoMoreInteractions(userService,userMapper,passwordEncoder);

        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertEquals(expectedResponse,response.getBody().getData().toString());
    }


    @Test
    @DisplayName("Given a username that already exists, return Bad Request")
    void givenUsernameThatAlreadyExistsReturnBadRequest(){
        final var userDtoRequest = getUserDtoRequest();
        final var userModel = getUserModel();
        final var expectedResponse = "This username already exists.";

        when(userService.findByUsername(userDtoRequest.getUsername())).thenReturn(Optional.of(userModel));

        final var response = userController.saveUser(userDtoRequest);

        verify(userService).findByUsername(userDtoRequest.getUsername());

        verifyNoMoreInteractions(userService);
        verifyNoInteractions(userMapper,passwordEncoder);

        assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
        assertEquals(expectedResponse,response.getBody().getData().toString());
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