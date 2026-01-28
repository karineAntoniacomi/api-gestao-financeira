package br.com.gestao.financeira.api.controller;

import br.com.gestao.financeira.api.domain.port.TransacaoRepository;
import br.com.gestao.financeira.api.exception.GlobalExceptionHandler;
import br.com.gestao.financeira.api.service.TransacaoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
//import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.assertj.core.api.Assertions.assertThat;

import static org.springframework.test.web.servlet.result.StatusResultMatchersExtensionsKt.isEqualTo;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@WebMvcTest(ResumoTransacaoController.class)
@Import(GlobalExceptionHandler.class)
class TransacaoControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private TransacaoService service;

    @MockitoBean
    private TransacaoRepository repository;

    @Test
    @DisplayName("Deveria devolver codigo http 400 quando informacoes estao invalidas")
    // @WithMockUser
    void cadastrar_cenario1() throws Exception {
//        var response = mvc
//                .perform(post("/transacoes"))
//                .andReturn().getResponse();
//
//        assertThat(response.getStatus))
//        .isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

}