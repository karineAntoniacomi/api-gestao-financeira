package br.com.gestao.financeira.service.usuario.domain.usuario.dto;

public record DadosImportacaoUsuario(
        String nome,
        String email,
        String cpf,
        String telefone,
        String profissao
) {}