package br.com.gestao.financeira.processorConsumer.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DadosDetalhamentoTransacao(
        Long id,
        TipoTransacao tipoTransacao,
        BigDecimal valor,
        LocalDateTime dataTransacao,
        Long usuarioId,
        StatusTransacao status
) {
}
