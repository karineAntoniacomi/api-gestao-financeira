package br.com.gestao.financeira.processor.consumer.repository;

import br.com.gestao.financeira.processor.consumer.model.DeadLetterTransacao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface DeadLetterTransacaoRepository extends JpaRepository<DeadLetterTransacao, UUID> {
}
