package br.com.gestao.financeira.service.transacao.controller;

import br.com.gestao.financeira.service.transacao.domain.Transacao;
import br.com.gestao.financeira.service.transacao.repository.TransacaoRepository;
import br.com.gestao.financeira.service.transacao.dto.DadosAtualizacaoTransacao;
import br.com.gestao.financeira.service.transacao.dto.DadosCadastroTransacao;
import br.com.gestao.financeira.service.transacao.dto.DadosDetalhamentoTransacao;
import br.com.gestao.financeira.service.transacao.dto.DadosListagemTransacao;
import br.com.gestao.financeira.service.transacao.service.MensageriaService;
import br.com.gestao.financeira.service.transacao.service.TransacaoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;

@RestController
@RequestMapping("/transacoes")
@SecurityRequirement(name = "bearer-key")
public class TransacaoController {

    @Autowired
    private TransacaoRepository repository;

    @Autowired
    private TransacaoService transacaoService;

    @Autowired
    private MensageriaService mensageriaService;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(
            @RequestBody @Valid DadosCadastroTransacao dados,
            UriComponentsBuilder uriBuilder) {
        
        var auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        String usuarioId;
        if (auth.getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails) {
            usuarioId = ((org.springframework.security.core.userdetails.UserDetails) auth.getPrincipal()).getUsername();
        } else {
            usuarioId = auth.getPrincipal().toString();
        }
        
        var transacao = new Transacao(dados, Long.valueOf(usuarioId));
        repository.save(transacao);

        var uri = uriBuilder.path("/transacoes/{id}").buildAndExpand(transacao.getId()).toUri();

        var dto = new DadosDetalhamentoTransacao(transacao);
        mensageriaService.notificarTransacao(dto);

        return ResponseEntity.created(uri).body(dto);
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemTransacao>> listar(@PageableDefault(size = 10, sort = {"dataTransacao"}) Pageable paginacao) {
        // converte lista de usuários para lista do DTO
        var page = repository.findAllAtivas(paginacao);
        return ResponseEntity.ok(page);
    }

    @PatchMapping("/{id}")
    @Transactional
    public ResponseEntity<DadosDetalhamentoTransacao> atualizarParcial(
            @PathVariable Long id,
            @RequestBody DadosAtualizacaoTransacao dados) {

        Transacao transacao = transacaoService.atualizarParcial(id, dados);
        return ResponseEntity.ok(new DadosDetalhamentoTransacao(transacao));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DadosDetalhamentoTransacao> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid DadosAtualizacaoTransacao dados) {

        Transacao transacao = transacaoService.atualizar(id, dados);
        return ResponseEntity.ok(new DadosDetalhamentoTransacao(transacao));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluir(@PathVariable Long id) {
        transacaoService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity detalhar(@PathVariable Long id) {
        Transacao transacao = transacaoService.detalhar(id);
        return ResponseEntity.ok(new DadosDetalhamentoTransacao(transacao));
    }

    @GetMapping("/{id}/converter")
    public ResponseEntity<BigDecimal> converterValor(
            @PathVariable Long id,
            @RequestParam String moeda) {

        return ResponseEntity.ok(transacaoService.converter(id, moeda));
    }

}

