package com.example.API_User.Security;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

public class DescriptografarAPI_User {
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
            System.out.println("chave privada: " + Arrays.toString(privateKey.getEncoded()));
        } catch (Exception e) {
            System.out.println("Erro ao inicializar as chaves");
        }
    }


    public String descriptografarSecretKey(SecretKey secretKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decodificado = Base64.getDecoder().decode(secretKey.getEncoded());
            byte[] descriptografado = cipher.doFinal(decodificado);
            String result = new String(descriptografado);

            return result;
        } catch (Exception e) {
            System.out.println("Erro ao descriptografar secretKey: " + e.getMessage());
            return null;
        }
    }

    public String descriptografarBody(String payload, SecretKey secretKey) {
        try {
            byte[] encryptedBodyBytes = Base64.getDecoder().decode(payload);

            Cipher aesCipher = Cipher.getInstance("AES");
            aesCipher.init(Cipher.DECRYPT_MODE, secretKey);

            byte[] decrypteBodyBytes = aesCipher.doFinal(encryptedBodyBytes);
            return Base64.getEncoder().encodeToString(decrypteBodyBytes);
        } catch (Exception e) {
            System.out.println("Erro ao descriptografar payload: " + e.getMessage());
            return null;
        }
    }

    public String descriptografarResponse(ResponseEntity<?> response) {
        String chaveHeaderBase64 = response.getHeaders().getFirst("secret_key");

        if (chaveHeaderBase64 == null) return null;
        if (response.getBody() == null) return null;

        byte[] chaveHeader = Base64.getEncoder().encode(chaveHeaderBase64.getBytes());

        try {
            Cipher cipherHeader = Cipher.getInstance("RSA");
            cipherHeader.init(Cipher.DECRYPT_MODE, privateKey);

            chaveHeader = cipherHeader.doFinal(chaveHeader);
            SecretKey secretKey = new SecretKeySpec(chaveHeader, "AES");

            Cipher cipherBody = Cipher.getInstance("AES");
            cipherBody.init(Cipher.DECRYPT_MODE, secretKey);

            byte[] body =  cipherBody.doFinal(response.getBody().toString().getBytes());

            return new String(body);
        } catch (Exception e) {
            System.out.println("Erro ao descriptografar Response: " + e.getMessage());
            return null;
        }
    }
}
