package br.com.gestao.financeira.processor.consumer.service;

import br.com.gestao.financeira.processor.consumer.dto.DadosDetalhamentoTransacao;
import br.com.gestao.financeira.processor.consumer.model.DeadLetterTransacao;
import br.com.gestao.financeira.processor.consumer.repository.DeadLetterTransacaoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeadLetterService {

    private final DeadLetterTransacaoRepository repository;
    private final ObjectMapper objectMapper;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void salvar(DadosDetalhamentoTransacao dados, Exception ex, String tipoErro, String topico) {
        try {
            String payload = objectMapper.writeValueAsString(dados);
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            
            DeadLetterTransacao dlq = DeadLetterTransacao.builder()
                    .id(UUID.randomUUID())
                    .transacaoId(dados.id())
                    .usuarioId(dados.usuarioId())
                    .payload(payload)
                    .erro(ex.getMessage())
                    .stacktrace(sw.toString())
                    .tipoErro(tipoErro)
                    .tentativas(1)
                    .status("ERROR")
                    .topicoOrigem(topico)
                    .createdAt(LocalDateTime.now())
                    .lastAttemptAt(LocalDateTime.now())
                    .build();

            repository.save(dlq);
            log.info("Erro de negócio salvo na DeadLetter para a transação: {}", dados.id());
        } catch (Exception e) {
            log.error("Erro ao salvar DeadLetter para a transação {}: {}", dados.id(), e.getMessage());
        }
    }
}
