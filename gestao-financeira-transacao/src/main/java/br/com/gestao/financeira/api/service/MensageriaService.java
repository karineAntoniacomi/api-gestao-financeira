package br.com.gestao.financeira.api.service;

import br.com.gestao.financeira.api.dto.DadosDetalhamentoTransacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class MensageriaService {

    @Autowired
    private KafkaTemplate<String, DadosDetalhamentoTransacao> kafkaTemplate;

    public void notificarTransacao(DadosDetalhamentoTransacao dados) {
        kafkaTemplate.send("transaction.requested", dados);
    }
}
