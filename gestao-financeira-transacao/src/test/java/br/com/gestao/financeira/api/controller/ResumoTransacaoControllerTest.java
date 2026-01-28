package br.com.gestao.financeira.api.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import br.com.gestao.financeira.api.service.ResumoTransacaoService;
import java.util.List;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import br.com.gestao.financeira.api.infra.TratadorDeErros;

@WebMvcTest(ResumoTransacaoController.class)
@Import(TratadorDeErros.class)
class ResumoTransacaoControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private ResumoTransacaoService service;

    @Test
    @DisplayName("Deveria devolver codigo http 404 quando usuarioId não é passado no path")
    void porDiaCenario1() throws Exception {
        mvc.perform(get("/transacoes/resumo/dia"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deveria devolver codigo http 200 quando informacoes estao validas no path")
    void porDiaCenario2() throws Exception {
        when(service.resumoPorDia(anyLong())).thenReturn(List.of());

        mvc.perform(get("/transacoes/resumo/1/dia"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deveria devolver codigo http 404 quando usuarioId não é passado no path para resumo por categoria")
    void porCategoriaCenario1() throws Exception {
        mvc.perform(get("/transacoes/resumo/categoria"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deveria devolver codigo http 404 quando usuarioId não é passado no path para resumo por mes")
    void porMesCenario1() throws Exception {
        mvc.perform(get("/transacoes/resumo/mes"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deveria devolver codigo http 200 quando informacoes estao validas no path para resumo por categoria")
    void porCategoriaCenario2() throws Exception {
        when(service.resumoPorCategoria(anyLong())).thenReturn(List.of());

        mvc.perform(get("/transacoes/resumo/1/categoria"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deveria devolver codigo http 200 quando informacoes estao validas no path para resumo por mes")
    void porMesCenario2() throws Exception {
        when(service.resumoPorMes(anyLong())).thenReturn(List.of());

        mvc.perform(get("/transacoes/resumo/1/mes"))
                .andExpect(status().isOk());
    }
}
