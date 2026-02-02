package br.com.gestao.financeira.service.transacao.dto;

import br.com.gestao.financeira.service.transacao.domain.TipoTransacao;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DadosCadastroTransacao(
        @NotNull(message = "{tipoTransacao.obrigatoria}")
        TipoTransacao tipoTransacao,

        @NotNull(message = "{valor.obrigatorio}")
        BigDecimal valor,

        @NotNull(message = "{dataTransacao.obrigatoria}")
        LocalDateTime dataTransacao) {
}
