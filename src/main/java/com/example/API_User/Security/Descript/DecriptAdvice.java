package com.example.API_User.Security.Descript;

import com.example.API_User.Entity.Registro;
import com.example.API_User.Entity.RegistroCriptografado;
import com.example.API_User.Entity.RegistroFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@ControllerAdvice
public class DecriptAdvice extends RequestBodyAdviceAdapter {
    @Autowired
    private RegistroFactory registroFactory;

    // 1. Define onde esse Advice vai rodar
    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage,
                                           MethodParameter parameter,
                                           Type targetType,
                                           Class<? extends HttpMessageConverter<?>> converterType) throws IOException {

        if (!Objects.equals(inputMessage.getHeaders().getFirst("api_cliente"), "POSTMAN")) {
            String bodyOriginal = new String(inputMessage.getBody().readAllBytes(), StandardCharsets.UTF_8);

            RegistroCriptografado registroCriptografado = StringToRegistroCript(bodyOriginal);

            Registro registro = DescriptografarBody(registroCriptografado);
            ObjectMapper mapper = new ObjectMapper();

            return new HttpInputMessage() {
                @Override
                public InputStream getBody() {
                    try {
                        if (registro != null) {
                            String jsonFinal = mapper.writeValueAsString(registro);
                            return new ByteArrayInputStream(jsonFinal.getBytes(StandardCharsets.UTF_8));
                        }

                        return new ByteArrayInputStream("".getBytes());
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        return new ByteArrayInputStream("".getBytes());
                    }
                }

                @Override
                public HttpHeaders getHeaders() {
                    return inputMessage.getHeaders();
                }
            };
        }

        return inputMessage;
    }

    private RegistroCriptografado StringToRegistroCript(String corpoCriptografadoSujo) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(corpoCriptografadoSujo);

            if (jsonNode.has("cpf")) {
                return mapper.readValue(corpoCriptografadoSujo, RegistroCriptografado.class);
            }

            return null;
        } catch (IOException e) {
            System.out.println("Erro ao passar String para Object: " + e.getMessage());
            return null;
        }
    }

    private Registro DescriptografarBody(RegistroCriptografado jsonSujo) {
        return registroFactory.criarRegistroDescriptografado(jsonSujo);
    }
}