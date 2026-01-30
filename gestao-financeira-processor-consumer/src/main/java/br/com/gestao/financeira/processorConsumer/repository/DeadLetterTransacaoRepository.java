package br.com.gestao.financeira.processorConsumer.repository;

import br.com.gestao.financeira.processorConsumer.model.DeadLetterTransacao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface DeadLetterTransacaoRepository extends JpaRepository<DeadLetterTransacao, UUID> {
}
