package com.example.API_User.Repository;

import com.example.API_User.Entity.Permissao;
import com.example.API_User.Entity.Registro;
import com.example.API_User.Entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistroRepository extends JpaRepository<Registro, Integer> {
    public List<Registro> findByNome(String nome);
    public Optional<Registro> findByCpf(String cpf);
    public Optional<Registro> findByEmail(String email);
    public List<Optional<Registro>> findByCpfOrEmail(String cpf, String email);
    public List<Registro> findAllByStatus(Status status);
    public List<Registro> findAllByPermissao(Permissao permissao);
}
