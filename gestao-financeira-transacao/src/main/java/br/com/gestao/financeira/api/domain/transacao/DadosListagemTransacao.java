package br.com.gestao.financeira.api.domain.transacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DadosListagemTransacao(
        Long id,
        TipoTransacao tipoTransacao,
        BigDecimal valor,
        LocalDateTime dataTransacao,
        Long usuarioId
) {}

