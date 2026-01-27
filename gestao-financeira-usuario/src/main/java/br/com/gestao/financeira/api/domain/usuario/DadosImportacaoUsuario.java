package br.com.gestao.financeira.api.domain.usuario;

public record DadosImportacaoUsuario(
        String nome,
        String email,
        String cpf,
        String telefone,
        String profissao,
        String logradouro,
        String bairro,
        String cep,
        String cidade,
        String uf,
        String numero,
        String complemento
) {}