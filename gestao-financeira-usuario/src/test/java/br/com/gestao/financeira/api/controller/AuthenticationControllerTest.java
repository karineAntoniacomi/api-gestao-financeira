package br.com.gestao.financeira.api.controller;

import br.com.gestao.financeira.api.domain.usuario.dto.DadosAutenticacao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthenticationControllerTest {

    private AuthenticationController authenticationController;
    private AuthenticationManager authenticationManager;

    @BeforeEach
    void setUp() {
        authenticationManager = Mockito.mock(AuthenticationManager.class);
        authenticationController = new AuthenticationController();
        authenticationController.manager = authenticationManager;
    }

    @Test
    @DisplayName("Deveria devolver codigo http 200 quando login esta valid0")
    void login_cenario1() {
        DadosAutenticacao dados = new DadosAutenticacao("teste@email.com", "senha123");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mock(UsernamePasswordAuthenticationToken.class));

        ResponseEntity response = authenticationController.efetuarLogin(dados);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void login_cenario2_credenciais_invalidas() {
        DadosAutenticacao dados = new DadosAutenticacao("teste@email.com", "senhaErrada");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Credenciais de login invalidas"));

        try {
            authenticationController.efetuarLogin(dados);
        } catch (BadCredentialsException e) {
            assertEquals("Credenciais de login invalidas", e.getMessage());
        }

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }
}