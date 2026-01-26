package br.com.gestao.financeira.api.domain.transacao;


import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DadosDetalhamentoTransacao(Long id, TipoTransacao tipoTransacao, BigDecimal valor, LocalDateTime dataTransacao, Long usuarioId) {

    public DadosDetalhamentoTransacao(Transacao transacao) {
        this(transacao.getId(), transacao.getTipoTransacao(), transacao.getValor(), transacao.getDataTransacao(), transacao.getUsuarioId());
    }
}
