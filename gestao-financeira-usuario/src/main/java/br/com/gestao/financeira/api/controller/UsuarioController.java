package br.com.gestao.financeira.api.controller;

import br.com.gestao.financeira.api.domain.usuario.Usuario;
import br.com.gestao.financeira.api.domain.usuario.UsuarioRepository;
import br.com.gestao.financeira.api.domain.usuario.dto.DadosAtualizacaoUsuario;
import br.com.gestao.financeira.api.domain.usuario.dto.DadosCadastroUsuario;
import br.com.gestao.financeira.api.domain.usuario.dto.DadosDetalhamentoUsuario;
import br.com.gestao.financeira.api.domain.usuario.dto.DadosListagemUsuario;
import br.com.gestao.financeira.api.service.UsuarioService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/usuarios")
@SecurityRequirement(name = "bearer-key")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroUsuario dados, UriComponentsBuilder uriBuilder) {
        var usuario = new Usuario(dados);
        repository.save(usuario);

        var uri = uriBuilder.path("/usuarios/{id}").buildAndExpand(usuario.getId()).toUri();

        return ResponseEntity.created(uri).body(new DadosDetalhamentoUsuario(usuario));
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemUsuario>> listar(
            @PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao
    ) {
        var page = service.listarUsuariosComSaldo(paginacao);
        return ResponseEntity.ok(page);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DadosDetalhamentoUsuario> atualizar(@PathVariable Long id,
                                                              @RequestBody @Valid DadosAtualizacaoUsuario dados,
                                                              @AuthenticationPrincipal Usuario usuarioAutenticado) {
        assertSelf(id, usuarioAutenticado);

        var usuario = repository.getReferenceById(id);
        usuario.atualizarInformacoes(dados);

        return ResponseEntity.ok(new DadosDetalhamentoUsuario(usuario));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluir(@PathVariable Long id,
                                  @AuthenticationPrincipal Usuario usuarioAutenticado) {
        assertSelf(id, usuarioAutenticado);

        var usuario = repository.getReferenceById(id);
        usuario.excluir();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Transactional
    public ResponseEntity detalhar(@PathVariable Long id,
                                   @AuthenticationPrincipal Usuario usuarioAutenticado) {
        assertSelf(id, usuarioAutenticado);

        var usuario = repository.getReferenceById(id);
        return ResponseEntity.ok(new DadosDetalhamentoUsuario(usuario));
    }

    private void assertSelf(Long requestedUserId, Usuario usuarioAutenticado) {
        if (usuarioAutenticado == null || usuarioAutenticado.getId() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não autenticado");
        }
        if (!requestedUserId.equals(usuarioAutenticado.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não pode acessar dados de outro usuário");
        }
    }
}