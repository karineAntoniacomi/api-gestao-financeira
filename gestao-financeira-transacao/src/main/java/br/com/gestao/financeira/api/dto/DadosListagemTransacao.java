package br.com.gestao.financeira.api.dto;

import br.com.gestao.financeira.api.domain.StatusTransacao;
import br.com.gestao.financeira.api.domain.TipoTransacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DadosListagemTransacao(
        Long id,
        TipoTransacao tipoTransacao,
        BigDecimal valor,
        LocalDateTime dataTransacao,
        Long usuarioId,
        StatusTransacao status
) {}

