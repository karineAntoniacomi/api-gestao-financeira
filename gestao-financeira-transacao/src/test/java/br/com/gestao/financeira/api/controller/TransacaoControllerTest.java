package br.com.gestao.financeira.api.controller;

import br.com.gestao.financeira.api.domain.port.TransacaoRepository;
import br.com.gestao.financeira.api.exception.GlobalExceptionHandler;
import br.com.gestao.financeira.api.service.TransacaoService;
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

@WebMvcTest(TransacaoController.class)
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
    void cadastrar_cenario1() throws Exception {
        var json = """
                    {
                        "tipoTransacao": null,
                        "valor": null,
                        "dataTransacao": null,
                        "usuarioId": null
                    }
                """;

        MockHttpServletResponse response = mvc.perform(post("/transacoes")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andReturn().getResponse();

        assertEquals(400, response.getStatus());
    }

    @Test
    @DisplayName("Deveria devolver codigo http 201 quando transacao eh cadastrada com sucesso")
    void cadastrar_cenario2() throws Exception {
        Mockito.when(repository.save(Mockito.any())).thenAnswer(inv -> {
            var t = inv.getArgument(0);
            ReflectionTestUtils.setField(t, "id", 1L);
            return t;
        });
    }
}