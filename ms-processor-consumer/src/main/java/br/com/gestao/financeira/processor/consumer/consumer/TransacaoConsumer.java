package br.com.gestao.financeira.processor.consumer.consumer;

import br.com.gestao.financeira.processor.consumer.dto.DadosDetalhamentoTransacao;
import br.com.gestao.financeira.processor.consumer.service.TransacaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransacaoConsumer {

    private final TransacaoService service;

    @KafkaListener(topics = "transaction.requested", groupId = "grupo-1")
    public void consumir(DadosDetalhamentoTransacao dados, @Header(KafkaHeaders.RECEIVED_TOPIC) String topico) {
        log.info("Mensagem recebida do tópico {}: {}", topico, dados);
        service.processar(dados, topico);
    }
}
