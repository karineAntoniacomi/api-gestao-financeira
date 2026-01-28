package br.com.gestao.financeira.api.dto;

import br.com.gestao.financeira.api.domain.model.TipoTransacao;

import java.math.BigDecimal;

public record DadosResumoCategoria(
        TipoTransacao tipoTransacao,
        BigDecimal total
) {
}

