package br.com.gestao.financeira.api.domain.transacao;

import java.math.BigDecimal;

public record DadosCambio(
        String baseCurrency,
        String currency,
        BigDecimal rate
    ) {}
