package com.example.API_User.Configuracao;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.stereotype.Component;

@Component
public class Interceptador implements HandlerInterceptor {
    @Value("${api.user.key}")
    private String apiToken;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getHeader("api_token") != null && request.getHeader("api_token").equals(apiToken)) return true;

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        return false;
    }
}
