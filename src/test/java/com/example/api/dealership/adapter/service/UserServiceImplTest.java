package com.example.api.dealership.adapter.service;

import com.example.api.dealership.adapter.output.repository.port.UserRepositoryPort;
import com.example.api.dealership.adapter.service.user.impl.UserServiceImpl;
import com.example.api.dealership.core.domain.UserModel;
import com.example.api.dealership.core.exceptions.UsernameAlreadyUsedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    @DisplayName("Given a user save it in the database")
    void givenAUserSaveItInTheDatabase() {
        final var userModel = UserModel.builder().username("jhon").build();

        when(userRepositoryPort.findByUsername(userModel.getUsername())).thenReturn(Optional.empty());
        when(bCryptPasswordEncoder.encode(userModel.getPassword())).thenReturn("123");
        when(userRepositoryPort.save(userModel)).thenReturn(userModel);

        assertDoesNotThrow(() -> {
            final var savedUser = userService.saveUser(userModel);
            assertEquals(savedUser,userModel);
        });
    }

    @Test
    @DisplayName("When trying to save a user that already exists then throw UsernameAlreadyUsedException")
    void whenTryingToSaveUserThatAlreadyExistsThenThrowUsernameAlreadyUsedException() {
        UserModel userModel = UserModel.builder().username("jhon").build();

        when(userRepositoryPort.findByUsername(userModel.getUsername())).thenReturn(Optional.of(userModel));

        assertThrows(UsernameAlreadyUsedException.class,() -> userService.saveUser(userModel));
    }

    @Test
    @DisplayName("Given a valid username find it in the database")
    void givenValidUsernameFindItInTheDatabase() {
        final var username = "username";
        final var userModel = UserModel.builder().build();
        when(userRepositoryPort.findByUsername(username)).thenReturn(Optional.of(userModel));

        final var foundUser = userService.findByUsername(username);

        verify(userRepositoryPort).findByUsername(username);

        assertEquals(foundUser.get(),userModel);
    }
}
