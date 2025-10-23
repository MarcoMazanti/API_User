package com.example.API_User.Security;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.crypto.SecretKey;

@ControllerAdvice
public class CriptResponseAdivice implements ResponseBodyAdvice<Object> {
    private final Criptografar criptografar;

    public CriptResponseAdivice(Criptografar criptografar) {
        this.criptografar = criptografar;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {
        SecretKey secretKey = criptografar.generateAesKey();
        String encryptedBody = criptografar.criptografarBody(body.toString(), secretKey);
        response.getHeaders().set("secret_key", criptografar.criptografarSecretKey(secretKey));

        return encryptedBody;
    }
}
