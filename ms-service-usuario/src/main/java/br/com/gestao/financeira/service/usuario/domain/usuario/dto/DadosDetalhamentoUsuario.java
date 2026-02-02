package br.com.gestao.financeira.service.usuario.domain.usuario.dto;

import br.com.gestao.financeira.service.usuario.domain.usuario.Usuario;

public record DadosDetalhamentoUsuario(Long id,
                                       String nome,
                                       String email,
                                       String cpf,
                                       String telefone,
                                       String profissao) {

    public DadosDetalhamentoUsuario(Usuario usuario) {
        this(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getCpf(), usuario.getTelefone(), usuario.getProfissao());
    }
}
