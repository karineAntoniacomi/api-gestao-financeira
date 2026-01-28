package br.com.gestao.financeira.api.service;

import br.com.gestao.financeira.api.domain.model.Transacao;
import br.com.gestao.financeira.api.domain.port.TransacaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        return conversaoMoedaService.converter(transacao.getValor(), moeda);
    }
}

