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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

        String cliente = Optional.ofNullable(request.getHeaders().getFirst("api_cliente")).orElse("");

        if (!"POSTMAN".equals(cliente)) {
            if (body == null) return null;
            if (body instanceof List<?> lista) {
                if (!lista.isEmpty()) {
                    if (lista.get(0) instanceof Registro) {
                        List<RegistroCriptografado> listaCriptografada = new ArrayList<>();
                        for (Object item : lista) {
                            listaCriptografada.add(criarRegistroCriptografado((Registro) item));
                        }
                        return listaCriptografada;
                    }
                }
            } else {
                return criarRegistroCriptografado((Registro) body);
            }
        }

        return body;
    }

    private RegistroCriptografado criarRegistroCriptografado(Registro registro) {
        return registroFactory.criarRegistroCriptografado(registro);
    }
}
