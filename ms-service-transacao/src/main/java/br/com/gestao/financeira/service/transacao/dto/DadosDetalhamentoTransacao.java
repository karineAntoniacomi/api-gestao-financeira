package br.com.gestao.financeira.service.transacao.dto;


import br.com.gestao.financeira.service.transacao.domain.StatusTransacao;
import br.com.gestao.financeira.service.transacao.domain.TipoTransacao;
import br.com.gestao.financeira.service.transacao.domain.Transacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DadosDetalhamentoTransacao(Long id, TipoTransacao tipoTransacao, BigDecimal valor, LocalDateTime dataTransacao, Long usuarioId, StatusTransacao status) {

    public DadosDetalhamentoTransacao(Transacao transacao) {
        this(transacao.getId(), transacao.getTipoTransacao(), transacao.getValor(), transacao.getDataTransacao(), transacao.getUsuarioId(), transacao.getStatus());
    }
}
