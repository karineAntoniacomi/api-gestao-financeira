package br.com.gestao.financeira.api.service;

import br.com.gestao.financeira.api.domain.TipoTransacao;
import br.com.gestao.financeira.api.dto.DadosResumoCategoria;
import br.com.gestao.financeira.api.dto.DadosResumoMensal;
import br.com.gestao.financeira.api.dto.ResumoDiarioProjection;
import br.com.gestao.financeira.api.repository.TransacaoRepository;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class ResumoTransacaoServiceTest {

    private ResumoTransacaoService service;

    @Mock
    private TransacaoRepository repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new ResumoTransacaoService(repository);

        Long usuarioIdAutenticado = 1L;
        var authentication = new UsernamePasswordAuthenticationToken(usuarioIdAutenticado, null, List.of());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Deveria retornar resumo por categoria")
    void resumo_por_categoria_cenario1() {
        Long usuarioId = 1L;
        var resumo = new DadosResumoCategoria(TipoTransacao.COMPRA, new BigDecimal("100.00"));
        when(repository.totalPorCategoria(eq(usuarioId), anyList())).thenReturn(List.of(resumo));

        var resultado = service.resumoPorCategoria(usuarioId);

        assertEquals(1, resultado.size());
        assertEquals(TipoTransacao.COMPRA, resultado.get(0).tipoTransacao());
        assertEquals(new BigDecimal("100.00"), resultado.get(0).total());
    }

    @Test
    @DisplayName("Deveria retornar resumo por dia")
    void resumo_por_dia_cenario1() {
        Long usuarioId = 1L;
        LocalDate hoje = LocalDate.now();

        ResumoDiarioProjection projection = new ResumoDiarioProjection() {
            @Override
            public LocalDate getData() {
                return hoje;
            }

            @Override
            public BigDecimal getTotal() {
                return new BigDecimal("50.00");
            }
        };

        when(repository.totalPorDia(eq(usuarioId), anyList())).thenReturn(List.of(projection));

        var resultado = service.resumoPorDia(usuarioId);

        assertEquals(1, resultado.size());
        assertEquals(hoje, resultado.get(0).data());
        assertEquals(new BigDecimal("50.00"), resultado.get(0).total());
    }

    @Test
    @DisplayName("Deveria retornar resumo por mes")
    void resumo_por_mes_cenario1() {
        Long usuarioId = 1L;
        var resumo = new DadosResumoMensal(2026, 1, new BigDecimal("500.00"));
        when(repository.totalPorMes(eq(usuarioId), anyList())).thenReturn(List.of(resumo));

        var resultado = service.resumoPorMes(usuarioId);

        assertEquals(1, resultado.size());
        assertEquals(2026, resultado.get(0).ano());
        assertEquals(1, resultado.get(0).mes());
        assertEquals(new BigDecimal("500.00"), resultado.get(0).total());
    }
}