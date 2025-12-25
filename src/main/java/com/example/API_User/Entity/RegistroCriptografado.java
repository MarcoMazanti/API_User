package com.example.API_User.Entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record RegistroCriptografado(String id, String nome, String cpf, String email, String password, String telefone,
                                    String dataNascimento, String dataCadastro, String status, String permissao) {
    @JsonCreator
    public RegistroCriptografado(
            @JsonProperty("id") String id,
            @JsonProperty("nome") String nome,
            @JsonProperty("cpf") String cpf,
            @JsonProperty("email") String email,
            @JsonProperty("password") String password,
            @JsonProperty("telefone") String telefone,
            @JsonProperty("dataNascimento") String dataNascimento,
            @JsonProperty("dataCadastro") String dataCadastro,
            @JsonProperty("status") String status,
            @JsonProperty("permissao") String permissao
    ) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.password = password;
        this.telefone = telefone;
        this.dataNascimento = dataNascimento;
        this.dataCadastro = dataCadastro;
        this.status = status;
        this.permissao = permissao;
    }
}
