package com.example.api.dealership.adapter.service;

import com.example.api.dealership.adapter.output.repository.port.UserRepositoryPort;
import com.example.api.dealership.adapter.service.security.impl.UserDetailsServiceImpl;
import com.example.api.dealership.core.domain.UserModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private UserRepositoryPort userRepositoryPort;


    @Test
    @DisplayName("Given a valid username load the user that isn't a admin")
    void givenValidUsernameLoadTheUserThatIsNotAdmin(){
        final var userModel = getUserModel(false);
        final var username = "username";
        final var expectedUserDetails = getUserDetails(userModel);

        when(userRepositoryPort.findByUsername(username)).thenReturn(Optional.of(userModel));

        final var userDetails = userDetailsService.loadUserByUsername(username);

        verify(userRepositoryPort).findByUsername(username);


        Assertions.assertEquals(userDetails,expectedUserDetails);
    }

    @Test
    @DisplayName("Given a valid username load the user that is a admin")
    void givenValidUsernameLoadTheUserThatIsAdmin(){
        final var userModel = getUserModel(true);
        final var username = "username";
        final var expectedUserDetails = getUserDetails(userModel);

        when(userRepositoryPort.findByUsername(username)).thenReturn(Optional.of(userModel));

        final var userDetails = userDetailsService.loadUserByUsername(username);

        verify(userRepositoryPort).findByUsername(username);


        Assertions.assertEquals(userDetails,expectedUserDetails);
    }

    @Test
    @DisplayName("Given a invalid username throw the UsernameNotFoundException")
    void givenInvalidUsernameThrowUsernameNotFoundException(){
        final var username = "username";

        when(userRepositoryPort.findByUsername(username)).thenThrow(
                new UsernameNotFoundException("Username not found")
        );

        assertThrows(UsernameNotFoundException.class,() -> userDetailsService.loadUserByUsername(username));

        verify(userRepositoryPort).findByUsername(username);
    }

    private UserModel getUserModel(boolean isAdmin){
        return UserModel.builder()
                .username("username")
                .password("123456")
                .isAdmin(isAdmin)
                .id("id")
                .build();
    }

    private UserDetails getUserDetails(UserModel userModel){
        return User.builder()
                .username(userModel.getUsername())
                .password(userModel.getPassword())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .authorities(userDetailsService.getAuthorities(userModel.isAdmin()))
                .build();
    }

}