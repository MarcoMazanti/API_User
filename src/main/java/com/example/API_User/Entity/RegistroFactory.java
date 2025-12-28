package com.example.API_User.Entity;

import com.example.API_User.Security.Cript.Criptografar;
import com.example.API_User.Security.Descript.Descriptografar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class RegistroFactory {
    @Autowired
    private Descriptografar descriptografar;
    @Autowired
    private Criptografar criptografar;

    public Registro criarRegistroDescriptografado(RegistroCriptografado registroCriptografado) {
        Integer id = (registroCriptografado.id() != null) ? Integer.valueOf(descriptografar.descriptografarCampo(registroCriptografado.id())) : null;
        String nome = descriptografar.descriptografarCampo(registroCriptografado.nome());
        String cpf = descriptografar.descriptografarCampo(registroCriptografado.cpf());
        String email = descriptografar.descriptografarCampo(registroCriptografado.email());
        String password = descriptografar.descriptografarCampo(registroCriptografado.password());
        Long telefone = (registroCriptografado.telefone() != null) ? Long.valueOf(descriptografar.descriptografarCampo(registroCriptografado.telefone())) : null;

        Date dataNascimento = null;
        if (registroCriptografado.dataCadastro() != null) {
            String dataNascimentoStr = descriptografar.descriptografarCampo(registroCriptografado.dataNascimento()).trim();
            dataNascimento = new Date(Long.parseLong(dataNascimentoStr));
        }

        Date dataCadastro = null;
        if (registroCriptografado.dataCadastro() != null) {
            String dataCadastroStr = descriptografar.descriptografarCampo(registroCriptografado.dataCadastro()).trim();
            dataCadastro = new Date(Long.parseLong(dataCadastroStr));
        }

        Status status = null;
        if (registroCriptografado.status() != null) {
            String statusIndexStr = descriptografar.descriptografarCampo(registroCriptografado.status()).trim();
            int statusIndex = Integer.parseInt(statusIndexStr);
            status = Status.values()[statusIndex];
        }

        Permissao permissao = null;
        if (registroCriptografado.password() != null) {
            String permissaoIndexStr = descriptografar.descriptografarCampo(registroCriptografado.permissao()).trim();
            int permissaoIndex = Integer.parseInt(permissaoIndexStr);
            permissao = Permissao.values()[permissaoIndex];
        }

        return new Registro(id, nome, cpf, email, password, telefone, dataNascimento, dataCadastro, status, permissao);
    }

    public RegistroCriptografado criarRegistroCriptografado(Registro registro) {
        String id = (registro.getId() != null) ? criptografar.criptografarCampo(String.valueOf(registro.getId()).getBytes()) : null;
        String nome = criptografar.criptografarCampo(registro.getNome().getBytes());
        String cpf = criptografar.criptografarCampo(registro.getCpf().getBytes());
        String email = criptografar.criptografarCampo(registro.getEmail().getBytes());
        String password = criptografar.criptografarCampo(registro.getPassword().getBytes());
        String telefone = (registro.getTelefone() != null) ? criptografar.criptografarCampo(String.valueOf(registro.getTelefone()).getBytes()) : null;
        String dataNascimento = (registro.getDataNascimento() != null) ? criptografar.criptografarCampo(String.valueOf(registro.getDataNascimento().getTime()).getBytes()) : null;
        String dataCadastro = (registro.getDataCadastro() != null) ? criptografar.criptografarCampo(String.valueOf(registro.getDataCadastro().getTime()).getBytes()) : null;
        String status = (registro.getStatus() != null) ? criptografar.criptografarCampo(String.valueOf(registro.getStatus().ordinal()).getBytes()) : null;
        String permissao = (registro.getPermissao() != null) ? criptografar.criptografarCampo(String.valueOf(registro.getPermissao().ordinal()).getBytes()) : null;

        return new RegistroCriptografado(id, nome, cpf, email, password, telefone, dataNascimento, dataCadastro, status, permissao);
    }
}
