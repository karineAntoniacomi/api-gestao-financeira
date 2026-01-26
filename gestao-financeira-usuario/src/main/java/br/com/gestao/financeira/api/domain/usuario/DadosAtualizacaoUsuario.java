package br.com.gestao.financeira.api.domain.usuario;

import br.com.gestao.financeira.api.domain.endereco.DadosEndereco;
import jakarta.validation.Valid;

public record DadosAtualizacaoUsuario(
        Long id,
        String nome,
        String telefone,
        @Valid DadosEndereco endereco) {
}
