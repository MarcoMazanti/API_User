package com.example.API_User.Controller;

import com.example.API_User.Entity.Permissao;
import com.example.API_User.Entity.Registro;
import com.example.API_User.Entity.Status;
import com.example.API_User.Repository.RegistroRepository;
import com.example.API_User.Service.DadosRegistro;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.example.API_User.Security.EncriptacaoSenha.validarSenha;

@RestController
@RequestMapping("/api_user")
public class RegistroController {
    @Autowired
    private RegistroRepository registroRepository;
    @Autowired
    private DadosRegistro dadosRegistro;

    @GetMapping("/login/{cpf}/{senha}")
    public ResponseEntity<?> getLogin(@PathVariable String cpf, @PathVariable String senha) {
        try {
            List<Registro> listaRegistro = registroRepository.findAll();

            if (listaRegistro.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

            for (Registro registro : listaRegistro) {
                if (registro.getCpf().equals(cpf) && validarSenha(senha, registro.getPassword())) {
                    return ResponseEntity.status(HttpStatus.ACCEPTED).build();
                }
            }

            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Integer id) {
        try {
            Optional<Registro> registro = registroRepository.findById(id);

            if (registro.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            return ResponseEntity.ok(registro.get());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<?> getUserByNome(@PathVariable String nome) {
        try {
            List<Registro> listaRegistro = registroRepository.findByNome(nome);

            if (listaRegistro.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            return ResponseEntity.ok(listaRegistro);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/cpf/{cpf}")
    public  ResponseEntity<?> getUserByCpf(@PathVariable String cpf) {
        try {
            Optional<Registro> registro = registroRepository.findByCpf(cpf.replaceAll("\\.", "").replaceAll("-", ""));

            if (registro.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            return ResponseEntity.ok(registro.get());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        try {
            Optional<Registro> registro = registroRepository.findByEmail(email);

            if (registro.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            return ResponseEntity.ok(registro.get());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<?> getAllUsersByStatus(@PathVariable Status status) {
        try {
            List<Registro> listaRegistro = registroRepository.findAllByStatus(status);

            if (listaRegistro.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            return ResponseEntity.ok(listaRegistro);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/permissao/{permissao}")
    public ResponseEntity<?> getAllUsersByPermissao(@PathVariable Permissao permissao) {
        try {
            List<Registro> listaRegistro = registroRepository.findAllByPermissao(permissao);

            if (listaRegistro.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            return ResponseEntity.ok(listaRegistro);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/{idRequerinte}")
    public ResponseEntity<?> create(@PathVariable Integer idRequerinte, @RequestBody @Valid Registro registro) {
        try {
            Optional<Registro> registroOptionalCPF = registroRepository.findByCpf(registro.getCpf());
            Optional<Registro> registroOptionalEMAIL = registroRepository.findByEmail(registro.getEmail());

            if (registroOptionalCPF.isPresent()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("CPF já cadastrado!");
            if (registroOptionalEMAIL.isPresent()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email já cadastrado!");

            if (dadosRegistro.permissaoCriarConta(idRequerinte)) {
                Object retorno = registroRepository.save(registro);
                return ResponseEntity.status(HttpStatus.CREATED).body(retorno);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/{idRequerinte}")
    public ResponseEntity<?> update(@PathVariable Integer idRequerinte, @RequestBody @Valid Registro registro) {
        try {
            List<Optional<Registro>> registroOptional = registroRepository.findByCpfOrEmail(registro.getCpf(), registro.getEmail());

            if (!registroOptional.isEmpty()) {
                if (registroOptional.get(0).isPresent()) {
                    if (!Objects.equals(registroOptional.get(0).get().getId(), registro.getId())) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Campo único já utilizado!");
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Registro não encontrado!");
            }

            if (dadosRegistro.permissaoAlteracao(registro, idRequerinte)) {
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(registroRepository.save(registro));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/{idRequerinte}/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer idRequerinte, @PathVariable Integer id) {
        try {
            if (dadosRegistro.permissaoDeletarConta(idRequerinte)) {
                registroRepository.deleteById(id);
                return ResponseEntity.ok("Deletado com sucesso");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
            }
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
