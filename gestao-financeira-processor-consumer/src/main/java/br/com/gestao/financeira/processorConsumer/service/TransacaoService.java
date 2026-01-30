package br.com.gestao.financeira.processorConsumer.service;

import br.com.gestao.financeira.processorConsumer.exception.BusinessException;
import br.com.gestao.financeira.processorConsumer.dto.TipoTransacao;
import br.com.gestao.financeira.processorConsumer.dto.DadosDetalhamentoTransacao;
import br.com.gestao.financeira.processorConsumer.dto.StatusTransacao;
import br.com.gestao.financeira.processorConsumer.model.Transacao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransacaoService {

    private final KafkaTemplate<String, DadosDetalhamentoTransacao> kafkaTemplate;
    private final DeadLetterService deadLetterService;

    @Transactional
    public void processar(DadosDetalhamentoTransacao dados, String topico) {
        log.info("Processando transação: {}", dados.id());

        Transacao transacao = Transacao.toEntity(dados);

        try {
            // Consulta saldo / limites (OpenFinance) - Mock
            BigDecimal saldoDisponivel = consultarSaldoOpenFinance(dados.usuarioId());
            BigDecimal limiteDisponivel = consultarLimiteOpenFinance(dados.usuarioId());

            // Aplica regras
            if (validarRegrasNegocio(dados, saldoDisponivel, limiteDisponivel)) {
                // APPROVED
                transacao.setStatus(StatusTransacao.APPROVED);
                log.info("Transação {} APROVADA", dados.id());
            } else {
                // REJECTED
                transacao.setStatus(StatusTransacao.REJECTED);
                log.info("Transação {} REJEITADA por regras de negócio", dados.id());
            }
        } catch (BusinessException e) {
            log.error("Erro de negócio ao processar transação {}: {}", dados.id(), e.getMessage());
            deadLetterService.salvar(dados, e, "BUSINESS_ERROR", topico);
            return; // Interrompe para não salvar transação com status inválido ou enviar evento de sucesso
        } catch (Exception e) {
            log.error("Erro técnico ao processar transação {}: {}", dados.id(), e.getMessage());
            throw e; // Lança para o Kafka ErrorHandler tratar (retry/dlq kafka)
        }

        // Notifica o resultado
        enviarAtualizacaoStatus(transacao);
    }

    private boolean validarRegrasNegocio(DadosDetalhamentoTransacao dados, BigDecimal saldo, BigDecimal limite) {
        if (dados.valor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Valor da transação deve ser positivo");
        }

        // Tipo permite débito? (RETIRADA, COMPRA, TRANSFERENCIA = débitos)
        boolean isDebito = isTipoDebito(dados.tipoTransacao());

        if (isDebito) {
            // Saldo suficiente? (saldo + limite)
            BigDecimal totalDisponivel = saldo.add(limite);
            if (dados.valor().compareTo(totalDisponivel) > 0) {
                log.warn("Saldo insuficiente para transação {}. Disponível: {}, Requerido: {}", 
                        dados.id(), totalDisponivel, dados.valor());
                return false;
            }
        }

        // Limites adicionais? (ex: limite por transação de 10.000)
        if (dados.valor().compareTo(new BigDecimal("10000")) > 0) {
            log.warn("Valor da transação {} excede o limite permitido de 10.000", dados.id());
            return false;
        }

        return true;
    }

    private boolean isTipoDebito(TipoTransacao tipo) {
        return tipo == TipoTransacao.RETIRADA || 
               tipo == TipoTransacao.COMPRA || 
               tipo == TipoTransacao.TRANSFERENCIA;
    }

    private BigDecimal consultarSaldoOpenFinance(Long usuarioId) {
        // Mock de consulta OpenFinance
        if (usuarioId == null) {
            throw new BusinessException("Usuário não informado");
        }
        return new BigDecimal("5000.00");
    }

    private BigDecimal consultarLimiteOpenFinance(Long usuarioId) {
        // Mock de consulta OpenFinance
        return new BigDecimal("2000.00");
    }

    private void enviarAtualizacaoStatus(Transacao transacao) {
        DadosDetalhamentoTransacao dto = new DadosDetalhamentoTransacao(
                transacao.getId(),
                transacao.getTipoTransacao(),
                transacao.getValor(),
                transacao.getDataTransacao(),
                transacao.getUsuarioId(),
                transacao.getStatus()
        );
        kafkaTemplate.send("transaction.processed", dto);
    }
}
