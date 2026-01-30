package br.com.gestao.financeira.api.domain;

import br.com.gestao.financeira.api.dto.DadosAtualizacaoTransacao;
import br.com.gestao.financeira.api.dto.DadosCadastroTransacao;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// Entidade JPA
@Table(name = "transacoes")
@Entity(name = "Transacao")
// anotações Lombok
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long usuarioId;

    @Enumerated(EnumType.STRING)
    private TipoTransacao tipoTransacao;

    private BigDecimal valor;
    private Boolean ativo;

    @Enumerated(EnumType.STRING)
    private StatusTransacao status;

    @Past
    private LocalDateTime dataTransacao;

    public Transacao(DadosCadastroTransacao dados, Long usuarioId) {
        this.ativo = true;
        this.tipoTransacao = dados.tipoTransacao();
        this.valor = dados.valor();
        this.dataTransacao = dados.dataTransacao();
        this.usuarioId = usuarioId;
        this.status = StatusTransacao.PENDING;
    }

    public void atualizarInformacoes(@Valid DadosAtualizacaoTransacao dados) {
        if (dados.tipoTransacao() != null) {
            this.tipoTransacao = dados.tipoTransacao();
        }
        if (dados.valor() != null) {
            this.valor = dados.valor();
        }
        if (dados.dataTransacao() != null) {
            this.dataTransacao = dados.dataTransacao();
        }
    }

    public void excluir() {
        this.ativo = false;
    }

}
