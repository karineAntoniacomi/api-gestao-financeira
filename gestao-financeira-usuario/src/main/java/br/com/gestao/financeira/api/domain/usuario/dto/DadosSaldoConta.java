package br.com.gestao.financeira.api.domain.usuario.dto;

import java.math.BigDecimal;

public record DadosSaldoConta(Long usuarioId, BigDecimal saldo, String moeda, Boolean ativa) {

    public DadosSaldoConta {
        saldo = saldo != null ? saldo : BigDecimal.ZERO;
    }
}