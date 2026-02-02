package br.com.gestao.financeira.service.usuario.controller;

import br.com.gestao.financeira.service.usuario.domain.usuario.Usuario;
import br.com.gestao.financeira.service.usuario.domain.usuario.UsuarioRepository;
import br.com.gestao.financeira.service.usuario.security.TokenService;
import br.com.gestao.financeira.service.usuario.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.endsWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsuarioController.class)
@AutoConfigureMockMvc(addFilters = false)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UsuarioRepository repository;

    @MockitoBean
    private UsuarioService service;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("Deveria devolver codigo http 201 quando informacoes estao validas")
    void cadastrar_cenario1() throws Exception {
        // Arrange: save simula o JPA preenchendo o id
        when(repository.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario u = invocation.getArgument(0, Usuario.class);
            u.setId(1L);
            return u;
        });
        when(passwordEncoder.encode(any())).thenReturn("senha_criptografada");

        var body = """
            {
              "nome": "Maria Santos",
              "email": "marias@email.com",
              "cpf": "123.456.789-00",
              "telefone": "123456789",
              "senha": "senha123",
              "profissao": "Professora"
            }
            """;

        // Act + Assert
        mvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", endsWith("/usuarios/1")));

        verify(repository).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Deveria devolver codigo http 400 quando informacoes estao invalidas")
    void cadastrar_cenario2() throws Exception {
        // Arrange: campos inválidos
        var body = """
            {
              "nome": "",
              "email": "invalid-email",
              "cpf": "123",
              "telefone": "",
              "profissao": null
            }
            """;

        // Act + Assert
        mvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());

        verify(repository, never()).save(any());
    }
}