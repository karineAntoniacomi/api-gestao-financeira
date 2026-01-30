package br.com.gestao.financeira.processorConsumer.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "dead_letter_transacao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeadLetterTransacao {

    @Id
    private UUID id;

    private Long transacaoId;

    private Long usuarioId;

    @Column(columnDefinition = "jsonb")
    private String payload;

    private String erro;

    @Column(columnDefinition = "text")
    private String stacktrace;

    private String tipoErro;

    private Integer tentativas;

    private String status;

    private String topicoOrigem;

    private LocalDateTime createdAt;

    private LocalDateTime lastAttemptAt;
}
