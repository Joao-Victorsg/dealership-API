package com.example.api.dealership.config.rest.security;

import com.example.api.dealership.adapter.service.security.JwtService;
import com.example.api.dealership.adapter.service.user.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;

    public JwtAuthFilter(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        var authHeader = request.getHeader("Authorization");

        if(isBearer(authHeader)) {
            var token = getToken(authHeader);

            if (jwtService.isTokenValid(token)) {
                var username = jwtService.getUsername(token);
                var userDetail = userService.loadUserByUsername(username);

                var user = new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities());

                user.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(user);
            }
        }

        filterChain.doFilter(request,response);
    }

    private boolean isBearer(String authHeader) {
        return authHeader != null && authHeader.startsWith("Bearer");
    }

    private String getToken(String authHeader){
        return authHeader.split(" ")[1];
    }

}
