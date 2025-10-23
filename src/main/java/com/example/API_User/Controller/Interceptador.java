package com.example.API_User.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.stereotype.Component;

@Component
public class Interceptador implements HandlerInterceptor {
    @Value("${api.user.key}")
    private String apiSecret;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getHeader("API_USER_KEY") != null && request.getHeader("API_USER_KEY").equals(apiSecret)) return true;

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        return false;
    }
}
