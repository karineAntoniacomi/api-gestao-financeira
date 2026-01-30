package br.com.gestao.financeira.api.service;

import br.com.gestao.financeira.api.domain.TipoTransacao;
import br.com.gestao.financeira.api.dto.DadosResumoCategoria;
import br.com.gestao.financeira.api.dto.DadosResumoDiario;
import br.com.gestao.financeira.api.dto.DadosResumoMensal;
import br.com.gestao.financeira.api.repository.TransacaoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static br.com.gestao.financeira.api.domain.TipoTransacao.COMPRA;
import static br.com.gestao.financeira.api.domain.TipoTransacao.RETIRADA;
import static br.com.gestao.financeira.api.domain.TipoTransacao.TRANSFERENCIA;

@Service
public class ResumoTransacaoService {

    private final TransacaoRepository repository;

    private static final List<TipoTransacao> TIPOS_DESPESA =
            List.of(RETIRADA, TRANSFERENCIA, COMPRA);

    public ResumoTransacaoService(TransacaoRepository repository) {
        this.repository = repository;
    }

    public List<DadosResumoCategoria> resumoPorCategoria(Long usuarioId) {
        assertUsuarioAutorizado(usuarioId);
        return repository.totalPorCategoria(usuarioId, TIPOS_DESPESA);
    }

    public List<DadosResumoDiario> resumoPorDia(Long usuarioId) {
        assertUsuarioAutorizado(usuarioId);
        return repository.totalPorDia(usuarioId, TIPOS_DESPESA)
                .stream()
                .map(p -> new DadosResumoDiario(p.getData(), p.getTotal()))
                .toList();
    }

    public List<DadosResumoMensal> resumoPorMes(Long usuarioId) {
        assertUsuarioAutorizado(usuarioId);
        return repository.totalPorMes(usuarioId, TIPOS_DESPESA);
    }

    private void assertUsuarioAutorizado(Long usuarioIdDoPath) {
        Long usuarioIdAutenticado = getAuthenticatedUserIdOrNull();

        if (usuarioIdAutenticado == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não autenticado");
        }
        if (!usuarioIdAutenticado.equals(usuarioIdDoPath)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado ao usuário solicitado");
        }
    }

    private Long getAuthenticatedUserIdOrNull() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return null;

        Object principal = auth.getPrincipal();
        if (principal == null) return null;

        if (principal instanceof Long userId) {
            return userId;
        }
        if (principal instanceof Integer userId) { // só por garantia, caso alguém set como Integer
            return userId.longValue();
        }
        if (principal instanceof String userIdAsString) { // fallback defensivo
            try {
                return Long.valueOf(userIdAsString);
            } catch (NumberFormatException ex) {
                return null;
            }
        }

        return null;
    }
}