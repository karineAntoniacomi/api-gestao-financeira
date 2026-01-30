package br.com.gestao.financeira.api.dto;

import br.com.gestao.financeira.api.domain.TipoTransacao;

import java.math.BigDecimal;

public record DadosResumoCategoria(
        TipoTransacao tipoTransacao,
        BigDecimal total
) {
}

