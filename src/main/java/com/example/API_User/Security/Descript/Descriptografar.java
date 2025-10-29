package com.example.API_User.Security.Descript;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

@Component
public class Descriptografar {
    @Value("${chave.privada}")
    private String chavePrivadaString;

    private PrivateKey privateKey;

    @PostConstruct
    private void postConstruct() {
        try {
            // passo do modelo base64 para bytes[] a fim de poder criar as PrivateKey
            byte[] chavePrivadaBytes = Base64.getDecoder().decode(this.chavePrivadaString);

            // chave privada usa a conversão para PKCS8EncodedKeySpec
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(chavePrivadaBytes);

            // Criação da KeyFactory
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            // Geração final das chaves
            this.privateKey = keyFactory.generatePrivate(privateKeySpec);
        } catch (Exception e) {
            System.out.println("Erro ao inicializar as chaves");
        }
    }


    public SecretKey descriptografarSecretKey(String secretKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decodificado = Base64.getDecoder().decode(secretKey);
            byte[] descriptografado = cipher.doFinal(decodificado);
            String keyFinal = new String(descriptografado);

            return new SecretKeySpec(keyFinal.getBytes(), "AES");
        } catch (Exception e) {
            System.out.println("Erro ao descriptografar secretKey: " + e.getMessage());
            return null;
        }
    }

    public String descriptografarCampo(String campoCriptografado, SecretKey secretKey) {
        try {
            byte[] encryptedBodyBytes = Base64.getDecoder().decode(campoCriptografado);

            Cipher aesCipher = Cipher.getInstance("AES");
            aesCipher.init(Cipher.DECRYPT_MODE, secretKey);

            byte[] decrypteBodyBytes = aesCipher.doFinal(encryptedBodyBytes);
            return Base64.getEncoder().encodeToString(decrypteBodyBytes);
        } catch (Exception e) {
            System.out.println("Erro ao descriptografar payload: " + e.getMessage());
            return null;
        }
    }
}
