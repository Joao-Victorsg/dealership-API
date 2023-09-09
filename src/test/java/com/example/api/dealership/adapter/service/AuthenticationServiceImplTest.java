package com.example.api.dealership.adapter.service;

import com.example.api.dealership.adapter.dtos.user.UserDtoRequest;
import com.example.api.dealership.adapter.service.security.JwtService;
import com.example.api.dealership.adapter.service.security.impl.AuthenticationServiceImpl;
import com.example.api.dealership.adapter.service.security.impl.UserDetailsServiceImpl;
import com.example.api.dealership.core.domain.UserModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtService jwtService;
    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Test
    @DisplayName("Given a valid user return a valid token")
    void givenValidUserReturnValidToken(){
        final var userDto = UserDtoRequest.builder().username("username").password("123456").build();
        final var userModel = getUserModel(false);
        final var userDetails = getUserDetails(userModel);
        final var token = "token";

        when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDto.username(),
                        userDto.password())))
                .thenReturn(null);

        when(userDetailsService.loadUserByUsername(userDto.username())).thenReturn(userDetails);

        when(jwtService.generateToken(userDetails)).thenReturn(token);

        final var generatedToken = authenticationService.authenticate(userDto);

        assertEquals(generatedToken,token);
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
