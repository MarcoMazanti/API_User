package com.example.API_User.Entity;

import com.example.API_User.Security.Cript.Criptografar;
import com.example.API_User.Security.Descript.Descriptografar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class RegistroFactory {
    @Autowired
    private Descriptografar descriptografar;
    @Autowired
    private Criptografar criptografar;

    public Registro criarRegistroDescriptografado(RegistroCriptografado registroCriptografado) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        Integer id = Integer.valueOf(descriptografar.descriptografarCampo(registroCriptografado.id()));
        String nome = descriptografar.descriptografarCampo(registroCriptografado.nome());
        String cpf = descriptografar.descriptografarCampo(registroCriptografado.cpf());
        String email = descriptografar.descriptografarCampo(registroCriptografado.email());
        String password = descriptografar.descriptografarCampo(registroCriptografado.password());
        Long telefone = Long.valueOf(descriptografar.descriptografarCampo(registroCriptografado.telefone()));

        String dataNascimentoStr = descriptografar.descriptografarCampo(registroCriptografado.dataNascimento()).trim();
        Date dataNascimento = new Date(Long.parseLong(dataNascimentoStr));
        String dataCadastroStr = descriptografar.descriptografarCampo(registroCriptografado.dataCadastro()).trim();
        Date dataCadastro = new Date(Long.parseLong(dataCadastroStr));

        String statusIndexStr = descriptografar.descriptografarCampo(registroCriptografado.status()).trim();
        int statusIndex = Integer.parseInt(statusIndexStr);
        Status status = Status.values()[statusIndex];
        String permissaoIndexStr = descriptografar.descriptografarCampo(registroCriptografado.permissao()).trim();
        int permissaoIndex = Integer.parseInt(permissaoIndexStr);
        Permissao permissao = Permissao.values()[permissaoIndex];

        return new Registro(id, nome, cpf, email, password, telefone, dataNascimento, dataCadastro, status, permissao);
    }

    public RegistroCriptografado criarRegistroCriptografado(Registro registro) {
        String id = criptografar.criptografarCampo(String.valueOf(registro.getId()).getBytes());
        String nome = criptografar.criptografarCampo(registro.getNome().getBytes());
        String cpf = criptografar.criptografarCampo(registro.getCpf().getBytes());
        String email = criptografar.criptografarCampo(registro.getEmail().getBytes());
        String password = criptografar.criptografarCampo(registro.getPassword().getBytes());
        String telefone = criptografar.criptografarCampo(String.valueOf(registro.getTelefone()).getBytes());
        String dataNascimento = criptografar.criptografarCampo(String.valueOf(registro.getDataNascimento().getTime()).getBytes());
        String dataCadastro = criptografar.criptografarCampo(String.valueOf(registro.getDataCadastro().getTime()).getBytes());
        String status = criptografar.criptografarCampo(String.valueOf(registro.getStatus().ordinal()).getBytes());
        String permissao = criptografar.criptografarCampo(String.valueOf(registro.getPermissao().ordinal()).getBytes());

        return new RegistroCriptografado(id, nome, cpf, email, password, telefone, dataNascimento, dataCadastro, status, permissao);
    }
}
