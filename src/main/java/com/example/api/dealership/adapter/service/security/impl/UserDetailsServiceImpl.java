/*
package com.example.api.dealership.adapter.service.security.impl;

import com.example.api.dealership.adapter.output.repository.port.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepositoryPort userRepositoryPort;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var userModel = userRepositoryPort.findByUsername(username).
                orElseThrow(()-> new UsernameNotFoundException("Username not found"));

        return User.builder()
                .username(userModel.getUsername())
                .password(userModel.getPassword())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .authorities(getAuthorities(userModel.isAdmin()))
                .build();
    }

    public Collection<? extends GrantedAuthority> getAuthorities(boolean isAdmin) {
        final var authorities = new ArrayList<GrantedAuthority>();

        final var role = isAdmin ? "ADMIN" : "USER";

        authorities.add(new SimpleGrantedAuthority("ROLE_" + role));

        return authorities;
    }

}
*/
