package br.com.gestao.financeira.api.dto;

import java.math.BigDecimal;

public record DadosResumoMensal(
        int ano,
        int mes,
        BigDecimal total
) {
}
