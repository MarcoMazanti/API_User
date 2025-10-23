package com.example.API_User.Service;

import com.example.API_User.Entity.Permissao;
import com.example.API_User.Entity.Registro;
import com.example.API_User.Entity.Status;
import com.example.API_User.Repository.RegistroRepository;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class DadosRegistro {
    private final RegistroRepository registroRepository;

    public DadosRegistro(RegistroRepository registroRepository) {
        this.registroRepository = registroRepository;
    }

    private static final Map<Permissao, Set<String>> permissao = Map.of(
            Permissao.CLIENTE, Set.of("nome", "email", "password", "telefone", "dataNascimento"),
            Permissao.GERENTE, Set.of("nome", "email", "password", "telefone", "dataNascimento", "status", "permissao", "criar"),
            Permissao.ADMIN, Set.of("nome", "email", "password", "telefone", "dataNascimento", "status", "permissao", "criar", "deletar")
    );

    // Verifica se é permitido efetuar a alteração com base no Status e Permissão do Requerinte e do Alvo
    public boolean permissaoAlteracao(Registro registroAtualizado, int idRequerinte) {
        try {
            Registro registroRequerinte = registroRepository.getReferenceById(idRequerinte);
            Registro registroAntigo = registroRepository.getReferenceById(registroAtualizado.getId());

            if (registroRequerinte.getStatus() == Status.INATIVO) return false;

            for (Field campo : registroAtualizado.getClass().getDeclaredFields()) {
                campo.setAccessible(true);

                Object valorAntigo = campo.get(registroAntigo);
                Object valorNovo = campo.get(registroAtualizado);

                if (!Objects.equals(valorAntigo, valorNovo)) {
                    if (!permissaoPorCargo(registroAtualizado, registroRequerinte, campo.getName())) return false;
                }
            }

            return true;
        } catch (Exception e) {
            System.out.println("Erro na verificação de permissão de atualização de conta: " + e.getMessage());
            return false;
        }
    }

    // Verifica se é permitido efetuar a criação com base no Status e Permissão do Requerinte
    // Apenas GERENTE E ADMIN podem criar contas para terceiros
    public boolean permissaoCriarConta(Integer idRequerinte) {
        try {
            Registro registroRequerinte = registroRepository.getReferenceById(idRequerinte);

            if (registroRequerinte.getStatus() == Status.INATIVO) return false;

            return permissao.getOrDefault(registroRequerinte.getPermissao(), Set.of()).contains("criar");
        } catch (Exception e) {
            System.out.println("Erro na verificação de criação de conta: " +  e.getMessage());
            return false;
        }
    }

    // Verifica se é permitido deletar uma conta com base no Status e Permissão do Requerinte
    // Apenas o ADMIN pode deletar uma conta
    public boolean permissaoDeletarConta(Integer id ,Integer idRequerinte) {
        try {
            Registro registroRequerinte = registroRepository.getReferenceById(idRequerinte);
            Registro registroAlvo = registroRepository.getReferenceById(id);

            if (registroRequerinte.getStatus() == Status.INATIVO) return false;
            if (registroAlvo == null) return false;

            return permissaoPorCargo(registroAlvo, registroRequerinte, "deletar");
        } catch (Exception e) {
            System.out.println("Erro na verificação de exclusão de conta: " +  e.getMessage());
            return false;
        }
    }

    // Verifica se o Requerinte pode alterar ou não o nome do campo desejado
    private boolean permissaoPorCargo(Registro registroAtualizado, Registro registroRequerinte, String campo) {
        if (!Objects.equals(registroAtualizado.getId(), registroRequerinte.getId())) {
            if (registroRequerinte.getPermissao() == Permissao.ADMIN || registroAtualizado.getPermissao() == Permissao.ADMIN) {
                return permissao.get(Permissao.ADMIN).contains(campo);
            }
        }

        return permissao.getOrDefault(registroRequerinte.getPermissao(), Set.of()).contains(campo);
    }
}
