package com.example.API_User.Security.Cript;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class Criptografar {
    @Value("${chave.secreta}")
    private String secretKeyString;
    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        secretKey = new SecretKeySpec(Base64.getDecoder().decode(secretKeyString), "AES");
    }

    public String criptografarCampo(byte[] campo) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            byte[] campoCriptogrado = cipher.doFinal(campo);
            return Base64.getEncoder().encodeToString(campoCriptogrado);
        } catch (Exception e) {
            System.out.println("Erro ao criptografar payload: " + e.getMessage());
            return null;
        }
    }
}
