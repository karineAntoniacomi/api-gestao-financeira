package br.com.gestao.financeira.service.transacao.dto;

import br.com.gestao.financeira.service.transacao.domain.TipoTransacao;

import java.math.BigDecimal;

public record DadosResumoCategoria(
        TipoTransacao tipoTransacao,
        BigDecimal total
) {
}

