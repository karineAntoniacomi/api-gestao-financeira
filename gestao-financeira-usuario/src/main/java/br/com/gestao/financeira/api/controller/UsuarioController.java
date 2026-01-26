package br.com.gestao.financeira.api.controller;

import br.com.gestao.financeira.api.domain.usuario.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired // injeção de dependência - spring fica responsável por instanciar
    private UsuarioRepository repository;

    @PostMapping
    @Transactional
    public void cadastrar(@RequestBody @Valid DadosCadastroUsuario dados) {
        repository.save(new Usuario(dados));
    }

    @GetMapping
    public Page<DadosListagemUsuario> listar(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao) {
        // converte lista de usuários para lista do DTO
        return repository.findAllByAtivoTrue(paginacao).map(DadosListagemUsuario::new);
    }

    @PutMapping
    @Transactional
    public void atualizar(@RequestBody @Valid DadosAtualizacaoUsuario dados) {
        var usuario = repository.getReferenceById(dados.id());
        usuario.atualizarInformacoes(dados);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluir(@PathVariable Long id) {
        var usuario = repository.getReferenceById(id);
        usuario.excluir();
        return ResponseEntity.noContent().build();
    }
}

