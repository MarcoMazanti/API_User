package com.example.API_User.Security.Descript;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class Descriptografar {
    @Value("${chave.secreta}")
    private String secretKeyString;
    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        secretKey = new SecretKeySpec(Base64.getDecoder().decode(secretKeyString), "AES");
    }

    public String descriptografarCampo(String campoCriptografado) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            byte[] descriptografado = cipher.doFinal(Base64.getDecoder().decode(campoCriptografado));
            return new String(descriptografado, StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.out.println("Erro ao descriptografar payload: " + e.getMessage());
            return null;
        }
    }
}