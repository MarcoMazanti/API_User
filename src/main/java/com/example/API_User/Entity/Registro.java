package com.example.API_User.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

@Entity(name = "register")
@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class Registro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    private String nome;

    @NotBlank
    private String cpf;

    @Size(min = 10, max = 255)
    private String email;

    @Size(min = 6, max = 60)
    private String password;

    @Digits(integer = 15, fraction = 0)
    private Long telefone;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private Date dataNascimento;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private Date dataCadastro;

    private Status status;
    private Permissao permissao;

    @PrePersist
    private void onCreate() {
        if (this.dataCadastro == null) {
            this.dataCadastro = new Date();
        }
        if (this.status == null) {
            this.status = Status.ATIVO;
        }
        if (this.permissao == null) {
            this.permissao = Permissao.CLIENTE;
        }
    }

    public Registro(String nome, String cpf, String email, String password, Long telefone, Status status, Permissao permissao) {
        this.nome = nome;
        this.cpf = formatarCPF(cpf);
        this.email = email;
        this.password = password;
        this.telefone = telefone;
        this.status = status;
        this.permissao = permissao;
    }

    public Registro(String nome, String cpf, String email, String password, String telefone, Status status, Permissao permissao) {
        this.nome = nome;
        this.cpf = formatarCPF(cpf);
        this.email = email;
        this.password = password;
        this.telefone = formatarTelefone(telefone);
        this.status = status;
        this.permissao = permissao;
    }

    private String formatarCPF(String cpf) {
        return cpf.replaceAll("\\.", "").replaceAll("-", "");
    }

    private Long formatarTelefone(String telefone) {
        return Long.parseLong(telefone.replaceAll(" ", "").replaceAll("-", "").replaceAll("\\.", ""));
    }

    @Override
    public String toString() {
        return "Registro{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", cpf='" + cpf + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", telefone=" + telefone +
                ", dataNascimento=" + dataNascimento +
                ", dataCadastro=" + dataCadastro +
                ", status=" + status +
                ", permissao=" + permissao +
                '}';
    }
}
