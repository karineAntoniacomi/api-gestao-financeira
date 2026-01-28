package br.com.gestao.financeira.api.dto;

import br.com.gestao.financeira.api.domain.model.TipoTransacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DadosAtualizacaoTransacao(
        TipoTransacao tipoTransacao,
        BigDecimal valor,
        LocalDateTime dataTransacao) {
}
