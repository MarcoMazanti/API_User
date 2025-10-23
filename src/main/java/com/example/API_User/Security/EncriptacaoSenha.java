package com.example.API_User.Security;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class EncriptacaoSenha {
    public static String encriptarSenha(String senha) {
        return BCrypt.hashpw(senha, BCrypt.gensalt());
    }

    public static boolean validarSenha(String senhaFront, String senhaBancoDeDados) {
        return BCrypt.checkpw(senhaFront, senhaBancoDeDados);
    }

    public static boolean ehBcryptHash(String str) {
        if (str == null) return false;
        return str.matches("^\\$2[aby]\\$\\d{2}\\$[./A-Za-z0-9]{53}$");
    }
}
