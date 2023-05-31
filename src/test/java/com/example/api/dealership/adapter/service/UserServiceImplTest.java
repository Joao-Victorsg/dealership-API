package com.example.api.dealership.adapter.service;

import com.example.api.dealership.adapter.output.repository.port.UserRepositoryPort;
import com.example.api.dealership.adapter.service.user.impl.UserServiceImpl;
import com.example.api.dealership.core.domain.UserModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Test
    @DisplayName("Given a user save it in the database")
    void givenAUserSaveItInTheDatabase() {
        UserModel userModel = UserModel.builder().build();

        when(userRepositoryPort.save(userModel)).thenReturn(userModel);

        UserModel savedUser = userService.saveUser(userModel);

        verify(userRepositoryPort).save(userModel);
        verifyNoMoreInteractions(userRepositoryPort);

        assertEquals(savedUser,userModel);
    }

    @Test
    @DisplayName("Given a valid username found it in the database")
    void givenValidUsernameFoundItInTheDatabase() {
        final var username = "username";
        UserModel userModel = UserModel.builder().build();
        when(userRepositoryPort.findByUsername(username)).thenReturn(Optional.of(userModel));

        Optional<UserModel> foundUser = userService.findByUsername(username);

        verify(userRepositoryPort).findByUsername(username);

        assertEquals(foundUser.get(),userModel);
    }
}