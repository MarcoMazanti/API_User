package com.example.API_User.Security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

public class Criptografar {
    private PublicKey publicKey;

    public Criptografar(String tipoAPI) {
        try {
            String chaveAhUtilizar = switch (tipoAPI) {
                case "USER" ->
                        "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqFy9WZFw+z+ukUn3naBtiPFqwa6YKqvSqGIMjPVOZroVE38o3fdpa24LAvRxJiyoh/26rUajFCHDXuo/FOy1WRldHcTheAKwnrI6+7mVdwqBP3rJbn9tZmwMOOdOvDKDASnXdnziUsXkcZTplFc8VfglXHFm7/GNbKUUDktrNR5RioBPvBga6vZW3abVy9clE5Zvb5TKZMBSW1sXgn5kN0pqFq2rWzRBMdaszYMSsmowL9Cx4AKasB6xQHg3lGzcUkcvYU0rFYwqFBT3MvPyrqo9q1yJLzaw+7k0PTFbpCPwmr5Q+R94ffFCbwq+ip5BRjAZgBeXFCdj1MdJ6uzMFwIDAQAB";
                case "ARMAZEM" ->
                        "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA3T6yTZK/N2AeVuI7zcqNARlitc16tMHEJWRtRLt1QlgkLKzZ58xWdgAkFNkfsrE9wSX83pn5EJU4cDk4uNmqu7F/h/cq7p0hA/D0+cI/FFbb7kggqkoYCGq+1HA8QxgJfdLhSFOiBe/e1pN2QJxxgiRjNhxOPzmDHAKqq9qLVJjAGaiNZ2Gt+tJNFxn2x3BS8z/ABfJjpdCdrDXzS/977Urr7N4+ihCY9bkm3i8xrTxsJr8g4FfKJohVosaRY1tfKzevoxvBaC2p4Sh3/A5ERmyr1JKIw6W1uY77olrdszCDTg318sq8Yu7dhAT15kG620CHg1UClOYxZqWgB1f1QQIDAQAB";
                case "TRANSACAO" ->
                        "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0ztij8Rb3JPepO28AQCMYg4HwLchVslgIhYnUZxpfT1j8OnT2Hxcyf1kx8VYNttqYeeuZfDhWvEpI6NsoiCyhhMt+t4k5o7V7KIaVFXpQpvcovM88oYq+MuxtVSmfuVPBdOfE4vIhEFcQALF/b/flD1wdlUI8/gMvwihGORssyxjmJ46KVad68V1CCQykRijaE6DA86Do8fRfb9eHVlDJlubBWDq8jchDFN0hTBcSAl0UTa1lcGV0OBmRF8+sEmHAeE++spYENdKSnzd+By34PqA6YRIjS57KrAVT/qy96su+O1ll1d+lJqVIqCqW0Vr2WJ9ONrAS7FR1dkS+PFz8QIDAQAB";
                case "OPERACAO" ->
                        "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlH37dMszvZQOy6gWYuMf5IyvQXNYadbnosPKS/tqvyjnIHkFolRpdSvLvvuYinyA/K8a1jz1FpCG4/WlFX/k2GGF6WY3y8e4JjFXgOcqbk94zJNBBqJKUPCayzwNLkyKz++atzYUm7TOHkQpVJdkf9BkBmlZZdq12Urm2QxwXD9nVnGraCUpeyBEYZOee3sbCtqaLef8ga+3Xf6MbjsHKhLCgbcDsbrAHagmmKW2mMJdaO197D+OIfIHHvgNprD+ld6Ys2yvgF0Vx3x908oZ7FZG08joaOllmR/OEs2hsDfudCjqBo1kDn6zQ5XdMYtIaFbGw5ecHhqK17TGmj04dQIDAQAB";
                case "EXPERIENCIA" ->
                        "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqARh1ta38M2nmCxWRWJE+wTcRGwHFKv+TWOUAfkf/0O/11wRnEbxBdqmyaikMqlGqBWUKWRZORwzWRbzH+nWsGs4V4UEPaYsfdk2DtXNyAAYedrlXLw1FA9T5ldxZ/cNK98ZWcCckGBvHLgyicCDQgkO84o2vqlOo/26hnsUlCyZimpiOR3Va3r2g26+bRxb8EV9ycjHXKOHixxqsPUqfzGpXeNjUK8li0vsHCqUqGISHcr9YAY9ood724uaZ/hBbLZevVSuqJGx1cEa1WpCLkXP1TFJjU+BIM90cd0EXtkgP1txmvmdDXo2QSjaSZU5Gzfky6iYiXbV6BXYYBpUUQIDAQAB";
                case "AUTENTICACAO" ->
                        "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtGnLrq6t1QWqcRiJswaiSAgz0aKElhiFKi06vxTBXvRyepaxWUIqFQhNlv8UuE8VprO/C6oipqOcxB9j6vxn+RFLJyiVJCD/xjL3SpdsZ8seDTqGNG9B4rPoLctpjrgb5nxU12Cs015y1bfl5Q584Wrnk3b712MT+x4aVTLYiCNj7EXMYA0uNsz1ST6sTlJLQ+Btv6LMgycq/AplldKA7xjZqIOzoxiwfnCg2z85R2yhRgMJdBa3UnAvAz/frUbjtqgcDExFurPymAENLeizUMmpkl066NvAq6u7zURb4gAlQlAY5cLnLrMRftfcZyWsZujvlKkorXHhsLZxJ739IwIDAQAB";
                default -> null;
            };

            // passo do modelo base64 para bytes[] a fim de poder criar as PublicKey
            byte[] chavePublicaBytes = Base64.getDecoder().decode(chaveAhUtilizar);

            // chave pública usa a conversão para X509EncodedKeySpec
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(chavePublicaBytes);

            // Criação da KeyFactory
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            // Geração final das chaves
            this.publicKey = keyFactory.generatePublic(publicKeySpec);
            System.out.println("chave publica: " + Arrays.toString(publicKey.getEncoded()));
        } catch (Exception e) {
            System.out.println("Erro ao inicializar as chaves");
        }
    }

    public String criptografarSecretKey(SecretKey secretKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");

            System.out.println(Arrays.toString(publicKey.getEncoded()));
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            // encriptografa a mensagem e depois transforma na Base64 para poder passar pelo JSON do request HTTP
            byte[] criptografado = cipher.doFinal(secretKey.getEncoded());
            String base64 = Base64.getEncoder().encodeToString(criptografado);

            return base64;
        } catch (Exception e) {
            System.out.println("Erro ao criptografar secretKey: " + e.getMessage());
            return null;
        }
    }

    public String criptografarBody(String payload, SecretKey secretKey) {
        try {
            Cipher aesCipher = Cipher.getInstance("AES");
            aesCipher.init(Cipher.ENCRYPT_MODE, secretKey);

            byte[] encryptedBytes = aesCipher.doFinal(payload.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            System.out.println("Erro ao criptografar payload: " + e.getMessage());
            return null;
        }
    }

    public SecretKey generateAesKey() {
        try {
            // Usa o gerador de chaves para o algoritmo AES
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");

            // Inicializa o gerador com um tamanho de chave forte (256 bits)
            // Isso garante que a chave seja gerada a partir de uma fonte segura de aleatoriedade
            keyGen.init(256);

            // Gera a chave secreta
            return keyGen.generateKey();
        } catch (Exception e) {
            System.out.println("Erro ao generar AES key: " + e.getMessage());
            return null;
        }
    }
}
