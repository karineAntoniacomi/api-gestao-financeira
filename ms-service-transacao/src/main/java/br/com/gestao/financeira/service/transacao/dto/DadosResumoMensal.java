package br.com.gestao.financeira.service.transacao.dto;

import java.math.BigDecimal;

public record DadosResumoMensal(
        int ano,
        int mes,
        BigDecimal total
) {
}
