package com.example.API_User.Security.Cript;

import com.example.API_User.Entity.Registro;
import com.example.API_User.Entity.RegistroCriptografado;
import com.example.API_User.Entity.RegistroFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class CriptAdvice implements ResponseBodyAdvice<Object> {
    @Autowired
    private RegistroFactory registroFactory;

    @Override
    public boolean supports(MethodParameter returnType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {

        if (body == null) return null;
        if (body instanceof Registro bd) {
            RegistroCriptografado registroCriptografado = registroFactory.criarRegistroCriptografado(bd);
            System.out.println(registroCriptografado.toString());
            return registroCriptografado;
        }
        return body;
    }
}
