package br.com.gestao.financeira.api.dto;

import java.math.BigDecimal;

public record DadosCambio(
        String baseCurrency,
        String currency,
        BigDecimal rate
    ) {}
