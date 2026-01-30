package br.com.gestao.financeira.api.dto;


import br.com.gestao.financeira.api.domain.StatusTransacao;
import br.com.gestao.financeira.api.domain.TipoTransacao;
import br.com.gestao.financeira.api.domain.Transacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DadosDetalhamentoTransacao(Long id, TipoTransacao tipoTransacao, BigDecimal valor, LocalDateTime dataTransacao, Long usuarioId, StatusTransacao status) {

    public DadosDetalhamentoTransacao(Transacao transacao) {
        this(transacao.getId(), transacao.getTipoTransacao(), transacao.getValor(), transacao.getDataTransacao(), transacao.getUsuarioId(), transacao.getStatus());
    }
}
