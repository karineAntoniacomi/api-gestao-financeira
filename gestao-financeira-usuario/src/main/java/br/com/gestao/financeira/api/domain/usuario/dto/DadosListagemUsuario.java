package br.com.gestao.financeira.api.domain.usuario.dto;

import java.math.BigDecimal;

public record DadosListagemUsuario(Long id, String nome, String email, String cpf, BigDecimal saldo) {}
