package br.com.gestao.financeira.api.domain.usuario.dto;

import br.com.gestao.financeira.api.domain.endereco.Endereco;
import br.com.gestao.financeira.api.domain.usuario.Usuario;
import jakarta.validation.Valid;

public record DadosDetalhamentoUsuario(Long id,
                                       String nome,
                                       String email,
                                       String cpf,
                                       String telefone,
                                       String profissao,
                                       @Valid Endereco endereco) {

    public DadosDetalhamentoUsuario(Usuario usuario) {
        this(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getCpf(), usuario.getTelefone(), usuario.getProfissao(), usuario.getEndereco());
    }
}
