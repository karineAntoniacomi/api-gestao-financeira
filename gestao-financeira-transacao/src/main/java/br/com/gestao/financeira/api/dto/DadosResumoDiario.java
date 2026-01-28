package br.com.gestao.financeira.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DadosResumoDiario(
        LocalDate data,
        BigDecimal total
) {
}
