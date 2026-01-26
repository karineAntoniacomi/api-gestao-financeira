package br.com.gestao.financeira.api.domain.usuario;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SaldoContaDTO(Long usuarioId, BigDecimal saldo, String moeda, Boolean ativa) {

    public SaldoContaDTO {
        saldo = saldo != null ? saldo : BigDecimal.ZERO;
    }
}