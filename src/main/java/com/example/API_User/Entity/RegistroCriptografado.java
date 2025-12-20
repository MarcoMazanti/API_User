package com.example.API_User.Entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

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

        @Override
        public String toString() {
            return "RegistroCriptografado{" +
                    "id='" + id + '\'' +
                    ", nome='" + nome + '\'' +
                    ", cpf='" + cpf + '\'' +
                    ", email='" + email + '\'' +
                    ", password='" + password + '\'' +
                    ", telefone='" + telefone + '\'' +
                    ", dataNascimento='" + dataNascimento + '\'' +
                    ", dataCadastro='" + dataCadastro + '\'' +
                    ", status='" + status + '\'' +
                    ", permissao='" + permissao + '\'' +
                    '}';
        }
}
