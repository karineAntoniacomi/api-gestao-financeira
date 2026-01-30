package br.com.gestao.financeira.api.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import br.com.gestao.financeira.api.service.RelatorioService;
import br.com.gestao.financeira.api.service.ResumoTransacaoService;
import java.io.ByteArrayInputStream;
import java.util.List;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import br.com.gestao.financeira.api.exception.GlobalExceptionHandler;

@WebMvcTest(ResumoTransacaoController.class)
@Import(GlobalExceptionHandler.class)
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false)
@org.springframework.test.context.ActiveProfiles("test")
class ResumoTransacaoControllerTest {

    @MockitoBean
    private br.com.gestao.financeira.api.service.TokenService tokenService;

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private ResumoTransacaoService service;

    @MockitoBean
    private RelatorioService relatorioService;

    @Test
    @DisplayName("Deveria devolver codigo http 404 quando usuarioId não é passado no path")
    void por_dia_cenario1() throws Exception {
        mvc.perform(get("/transacoes/resumo/dia"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deveria devolver codigo http 200 quando informacoes estao validas no path")
    void por_dia_cenario2() throws Exception {
        when(service.resumoPorDia(anyLong())).thenReturn(List.of());

        mvc.perform(get("/transacoes/resumo/1/dia"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deveria devolver codigo http 404 quando usuarioId não é passado no path para resumo por categoria")
    void por_categoria_cenario1() throws Exception {
        mvc.perform(get("/transacoes/resumo/categoria"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deveria devolver codigo http 200 quando informacoes estao validas no path para resumo por categoria")
    void por_categoria_cenario2() throws Exception {
        when(service.resumoPorCategoria(anyLong())).thenReturn(List.of());

        mvc.perform(get("/transacoes/resumo/1/categoria"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deveria devolver codigo http 404 quando usuarioId não é passado no path para resumo por mes")
    void por_mes_cenario1() throws Exception {
        mvc.perform(get("/transacoes/resumo/mes"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deveria devolver codigo http 200 quando informacoes estao validas no path para resumo por mes")
    void por_mes_cenario2() throws Exception {
        when(service.resumoPorMes(anyLong())).thenReturn(List.of());

        mvc.perform(get("/transacoes/resumo/1/mes"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deveria devolver codigo http 200 ao baixar relatorio excel")
    void download_excel_cenario1() throws Exception {
        when(relatorioService.gerarExcel(anyLong())).thenReturn(new ByteArrayInputStream(new byte[0]));

        mvc.perform(get("/transacoes/resumo/1/relatorio/excel"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=resumo_transacoes.xlsx"))
                .andExpect(content().contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
    }

    @Test
    @DisplayName("Deveria devolver codigo http 200 ao baixar relatorio pdf")
    void download_pdf_cenario1() throws Exception {
        when(relatorioService.gerarPdf(anyLong())).thenReturn(new ByteArrayInputStream(new byte[0]));

        mvc.perform(get("/transacoes/resumo/1/relatorio/pdf"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=resumo_transacoes.pdf"))
                .andExpect(content().contentType("application/pdf"));
    }
}
