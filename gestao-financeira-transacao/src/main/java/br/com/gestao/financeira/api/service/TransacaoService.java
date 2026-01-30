package br.com.gestao.financeira.api.service;

import br.com.gestao.financeira.api.domain.Transacao;
import br.com.gestao.financeira.api.dto.DadosAtualizacaoTransacao;
import br.com.gestao.financeira.api.repository.TransacaoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@Service
public class TransacaoService {

    private final TransacaoRepository repository;
    private final ConversaoMoedaService conversaoMoedaService;

    public TransacaoService(TransacaoRepository repository, ConversaoMoedaService conversaoMoedaService) {
        this.repository = repository;
        this.conversaoMoedaService = conversaoMoedaService;
    }

    @Transactional(readOnly = true)
    public BigDecimal converter(Long id, String moeda) {
        Transacao transacao = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transação não encontrada"));

        assertUsuarioDonoDaTransacao(transacao);
        return conversaoMoedaService.converter(transacao.getValor(), moeda);
    }

    @Transactional(readOnly = true)
    public Transacao detalhar(Long id) {
        Transacao transacao = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transação não encontrada"));

        assertUsuarioDonoDaTransacao(transacao);
        return transacao;
    }

    @Transactional
    public Transacao atualizarParcial(Long id, DadosAtualizacaoTransacao dados) {
        Transacao transacao = repository.getReferenceById(id);
        assertUsuarioDonoDaTransacao(transacao);

        transacao.atualizarInformacoes(dados);
        return transacao;
    }

    @Transactional
    public Transacao atualizar(Long id, DadosAtualizacaoTransacao dados) {
        Transacao transacao = repository.getReferenceById(id);
        assertUsuarioDonoDaTransacao(transacao);

        transacao.atualizarInformacoes(dados);
        return transacao;
    }

    @Transactional
    public void excluir(Long id) {
        Transacao transacao = repository.getReferenceById(id);
        assertUsuarioDonoDaTransacao(transacao);

        transacao.excluir();
    }

    private void assertUsuarioDonoDaTransacao(Transacao transacao) {
        Long usuarioIdAutenticado = getAuthenticatedUserIdOrThrow();
        if (transacao.getUsuarioId() == null || !transacao.getUsuarioId().equals(usuarioIdAutenticado)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado à transação solicitada");
        }
    }

    private Long getAuthenticatedUserIdOrThrow() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não autenticado");
        }

        Object principal = auth.getPrincipal();
        if (principal instanceof Long userId) return userId;
        if (principal instanceof Integer userId) return userId.longValue();
        if (principal instanceof String userIdAsString) {
            try {
                return Long.valueOf(userIdAsString);
            } catch (NumberFormatException ex) {
                // principal inesperado
            }
        }

        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não autenticado");
    }
}