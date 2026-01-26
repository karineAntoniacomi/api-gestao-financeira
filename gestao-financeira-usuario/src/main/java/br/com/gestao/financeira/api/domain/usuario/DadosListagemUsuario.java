package br.com.gestao.financeira.api.domain.usuario;

import java.math.BigDecimal;

public record DadosListagemUsuario(Long id, String nome, String email, String cpf, BigDecimal saldo) {

    public DadosListagemUsuario(Usuario usuario) {
        this(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getCpf(), usuario.getSaldo());
    }
}
