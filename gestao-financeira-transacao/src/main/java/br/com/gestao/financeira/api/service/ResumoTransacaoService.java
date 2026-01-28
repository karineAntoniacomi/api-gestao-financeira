package br.com.gestao.financeira.api.service;

import br.com.gestao.financeira.api.domain.model.TipoTransacao;
import br.com.gestao.financeira.api.domain.port.TransacaoRepository;
import br.com.gestao.financeira.api.dto.DadosResumoCategoria;
import br.com.gestao.financeira.api.dto.DadosResumoDiario;
import br.com.gestao.financeira.api.dto.DadosResumoMensal;
import org.springframework.stereotype.Service;

import java.util.List;

import static br.com.gestao.financeira.api.domain.model.TipoTransacao.*;

@Service
public class ResumoTransacaoService {

    private final TransacaoRepository repository;

    private static final List<TipoTransacao> TIPOS_DESPESA =
            List.of(RETIRADA, TRANSFERENCIA, COMPRA);

    public ResumoTransacaoService(TransacaoRepository repository) {
        this.repository = repository;
    }

    public List<DadosResumoCategoria> resumoPorCategoria(Long usuarioId) {
        return repository.totalPorCategoria(usuarioId, TIPOS_DESPESA);
    }

    public List<DadosResumoDiario> resumoPorDia(Long usuarioId) {
        return repository.totalPorDia(usuarioId, TIPOS_DESPESA)
                .stream()
                .map(p -> new DadosResumoDiario(p.getData(), p.getTotal()))
                .toList();
    }

    public List<DadosResumoMensal> resumoPorMes(Long usuarioId) {
        return repository.totalPorMes(usuarioId, TIPOS_DESPESA);
    }
}
