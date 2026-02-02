package br.com.gestao.financeira.processor.consumer.model;

import br.com.gestao.financeira.processor.consumer.dto.DadosDetalhamentoTransacao;
import br.com.gestao.financeira.processor.consumer.dto.StatusTransacao;
import br.com.gestao.financeira.processor.consumer.dto.TipoTransacao;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class Transacao {

    private Long id;
    private Long usuarioId;

    private TipoTransacao tipoTransacao;

    private BigDecimal valor;
    private Boolean ativo;

    private StatusTransacao status;

    private LocalDateTime dataTransacao;

    public static Transacao toEntity(DadosDetalhamentoTransacao dados) {
        Transacao transacao = new Transacao();
        transacao.setId(dados.id());
        transacao.setUsuarioId(dados.usuarioId());
        transacao.setTipoTransacao(dados.tipoTransacao());
        transacao.setValor(dados.valor());
        transacao.setDataTransacao(dados.dataTransacao());
        transacao.setStatus(dados.status());
        transacao.setAtivo(true);
        return transacao;
    }

}
