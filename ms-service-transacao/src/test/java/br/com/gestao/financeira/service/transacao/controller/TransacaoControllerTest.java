package br.com.gestao.financeira.service.transacao.controller;

import br.com.gestao.financeira.service.transacao.domain.Transacao;
import br.com.gestao.financeira.service.transacao.repository.TransacaoRepository;
import br.com.gestao.financeira.service.transacao.exception.GlobalExceptionHandler;
import br.com.gestao.financeira.service.transacao.service.MensageriaService;
import br.com.gestao.financeira.service.transacao.service.TransacaoService;
import br.com.gestao.financeira.service.transacao.service.TokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.security.test.context.support.WithMockUser;

@WebMvcTest(TransacaoController.class)
@Import(GlobalExceptionHandler.class)
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
@org.springframework.test.context.ActiveProfiles("test")
class TransacaoControllerTest {

    @MockitoBean
    private TokenService tokenService;

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private TransacaoService service;

    @MockitoBean
    private TransacaoRepository repository;

    @Test
    @DisplayName("Deveria devolver codigo http 400 quando informacoes estao invalidas")
    @WithMockUser
    void cadastrar_cenario1() throws Exception {
        var json = """
                    {
                        "tipoTransacao": null,
                        "valor": null,
                        "dataTransacao": null
                    }
                """;

        MockHttpServletResponse response = mvc.perform(post("/transacoes")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                        .with(csrf()))
                .andReturn().getResponse();

        assertEquals(400, response.getStatus());
    }

    @MockitoBean
    private MensageriaService mensageriaService;

    @Test
    @DisplayName("Deveria devolver codigo http 201 e enviar notificacao quando transacao eh cadastrada com sucesso")
    @WithMockUser(username = "1")
    void cadastrar_cenario2() throws Exception {
        var json = """
                {
                    "tipoTransacao": "DEPOSITO",
                    "valor": 100.00,
                    "dataTransacao": "2024-01-28T10:00:00"
                }
                """;

        Mockito.when(repository.save(Mockito.any())).thenAnswer(inv -> {
            var t = (Transacao) inv.getArgument(0);
            ReflectionTestUtils.setField(t, "id", 1L);
            return t;
        });

        MockHttpServletResponse response = mvc.perform(post("/transacoes")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                        .with(csrf()))
                .andReturn().getResponse();

        assertEquals(201, response.getStatus());
        Mockito.verify(mensageriaService).notificarTransacao(Mockito.any());
        
        var responseContent = response.getContentAsString();
        org.junit.jupiter.api.Assertions.assertTrue(responseContent.contains("\"status\":\"PENDING\""));
    }
}