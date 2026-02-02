package br.com.gestao.financeira.service.transacao.dto;

import br.com.gestao.financeira.service.transacao.domain.TipoTransacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DadosAtualizacaoTransacao(
        TipoTransacao tipoTransacao,
        BigDecimal valor,
        LocalDateTime dataTransacao) {
}
