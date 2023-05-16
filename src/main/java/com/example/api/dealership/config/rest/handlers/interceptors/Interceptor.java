/*
package com.example.api.dealership.config.rest.handlers.interceptors;

import com.example.api.dealership.config.rest.token.validator.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class Interceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(request.getServletPath().contains("auths") || request.getServletPath().contains("users"))
            return true;

        return new Token(request.getHeader("token")).isValid();
    }
}
*/
