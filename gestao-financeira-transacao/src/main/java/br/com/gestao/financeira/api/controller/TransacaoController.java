package br.com.gestao.financeira.api.controller;

import br.com.gestao.financeira.api.domain.transacao.*;
import br.com.gestao.financeira.api.service.ConversaoMoedaService;
import br.com.gestao.financeira.api.service.TransacaoService;
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
public class TransacaoController {

    @Autowired // injeção de dependência - spring fica responsável por instanciar
    private TransacaoRepository repository;

    @Autowired
    private TransacaoService transacaoService;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroTransacao dados, UriComponentsBuilder uriBuilder) {
        var transacao = new Transacao(dados);
        repository.save(transacao);

        var uri = uriBuilder.path("/transacoes/{id}").buildAndExpand(transacao.getId()).toUri();

        return ResponseEntity.created(uri).body(new DadosDetalhamentoTransacao(transacao));
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
        var transacao = repository.getReferenceById(id);
        transacao.atualizarInformacoes(dados);
        return ResponseEntity.ok(new DadosDetalhamentoTransacao(transacao));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DadosDetalhamentoTransacao> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid DadosAtualizacaoTransacao dados) {
        var transacao = repository.getReferenceById(id);
        transacao.atualizarInformacoes(dados);
        return ResponseEntity.ok(new DadosDetalhamentoTransacao(transacao));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluir(@PathVariable Long id) {
        var transacao = repository.getReferenceById(id);
        transacao.excluir();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity detalhar(@PathVariable Long id) {
        var transacao = repository.getReferenceById(id);

        return ResponseEntity.ok(new DadosDetalhamentoTransacao(transacao));
    }

    @GetMapping("/{id}/converter")
    public ResponseEntity<BigDecimal> converterValor(
            @PathVariable Long id,
            @RequestParam String moeda) {

        return ResponseEntity.ok(transacaoService.converter(id, moeda));
    }

}

