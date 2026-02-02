package br.com.gestao.financeira.service.transacao.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DadosResumoDiario(
        LocalDate data,
        BigDecimal total
) {
}
