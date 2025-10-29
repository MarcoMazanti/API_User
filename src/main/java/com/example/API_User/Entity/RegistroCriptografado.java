package com.example.API_User.Entity;

import com.example.API_User.Security.Cript.Criptografar;
import lombok.Getter;

import javax.crypto.SecretKey;
import java.nio.ByteBuffer;

@Getter
public class RegistroCriptografado {
    private final String id;
    private final String nome;
    private final String cpf;
    private final String email;
    private final String password;
    private final String telefone;
    private final String dataNascimento;
    private final String dataCadastro;
    private final String status;
    private final String permissao;

    public RegistroCriptografado(Registro registro, SecretKey secretKey) {
        Criptografar criptografar = new Criptografar("USER");

        this.id = criptografar.criptografarCampo(ByteBuffer.allocate(4).putInt(registro.getId()).array(), secretKey);
        this.nome = criptografar.criptografarCampo(registro.getNome().getBytes(), secretKey);
        this.cpf = criptografar.criptografarCampo(registro.getCpf().getBytes(), secretKey);
        this.email = criptografar.criptografarCampo(registro.getEmail().getBytes(), secretKey);
        this.password = criptografar.criptografarCampo(registro.getPassword().getBytes(), secretKey);
        this.telefone = criptografar.criptografarCampo(ByteBuffer.allocate(8).putLong(registro.getTelefone()).array(), secretKey);
        this.dataNascimento = criptografar.criptografarCampo(ByteBuffer.allocate(8).putLong(registro.getDataNascimento().getTime()).array(), secretKey);
        this.dataCadastro = criptografar.criptografarCampo(ByteBuffer.allocate(8).putLong(registro.getDataCadastro().getTime()).array(), secretKey);
        this.status = criptografar.criptografarCampo(ByteBuffer.allocate(4).putInt(registro.getStatus().ordinal()).array(), secretKey);
        this.permissao = criptografar.criptografarCampo(ByteBuffer.allocate(4).putInt(registro.getPermissao().ordinal()).array(), secretKey);
    }
}
