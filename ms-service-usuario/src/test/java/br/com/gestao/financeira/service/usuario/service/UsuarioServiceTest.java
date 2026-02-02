package br.com.gestao.financeira.service.usuario.service;

import br.com.gestao.financeira.service.usuario.domain.usuario.dto.DadosListagemUsuario;
import br.com.gestao.financeira.service.usuario.domain.usuario.dto.DadosSaldoConta;
import br.com.gestao.financeira.service.usuario.domain.usuario.Usuario;
import br.com.gestao.financeira.service.usuario.domain.usuario.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class UsuarioServiceTest {

    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ContaClient contaClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usuarioService = new UsuarioService(usuarioRepository, contaClient);
    }

    @Test
    @DisplayName("deveria listar usuários com saldo quando existir usuario ativo e saldo")
    void listar_usuarios_com_saldo_cenario1() {
        // Arrange
        Usuario usuario1 = new Usuario();
        usuario1.setId(1L);
        usuario1.setNome("João Silva");
        usuario1.setEmail("joao@email.com");
        usuario1.setCpf("12345678901");
        usuario1.setAtivo(true);

        Usuario usuario2 = new Usuario();
        usuario2.setId(2L);
        usuario2.setNome("Maria Souza");
        usuario2.setEmail("maria@email.com");
        usuario2.setCpf("09876543210");
        usuario2.setAtivo(true);

        List<Usuario> usuarios = Arrays.asList(usuario1, usuario2);
        Page<Usuario> usuariosPage = new PageImpl<>(usuarios);

        DadosSaldoConta saldo1 = new DadosSaldoConta(1L, new BigDecimal("100.50"), "BRL", true);
        DadosSaldoConta saldo2 = new DadosSaldoConta(2L, new BigDecimal("250.75"), "BRL", true);
        List<DadosSaldoConta> saldos = Arrays.asList(saldo1, saldo2);

        when(usuarioRepository.findAllByAtivoTrue(any(Pageable.class))).thenReturn(usuariosPage);
        when(contaClient.listarContas()).thenReturn(saldos);

        // Act
        Page<DadosListagemUsuario> result = usuarioService.listarUsuariosComSaldo(Pageable.unpaged());

        // Assert
        assertEquals(2, result.getTotalElements());
        DadosListagemUsuario dto1 = result.getContent().get(0);
        assertEquals(usuario1.getId(), dto1.id());
        assertEquals(usuario1.getNome(), dto1.nome());
        assertEquals(usuario1.getEmail(), dto1.email());
        assertEquals(usuario1.getCpf(), dto1.cpf());
        assertEquals(new BigDecimal("100.50"), dto1.saldo());

        DadosListagemUsuario dto2 = result.getContent().get(1);
        assertEquals(usuario2.getId(), dto2.id());
        assertEquals(new BigDecimal("250.75"), dto2.saldo());
    }

    @Test
    @DisplayName("Deveria retornar pagina vazia quando nao houver usuario ativo ou saldo")
    void listarUsuariosComSaldo_cenario2() {
        // Arrange
        when(usuarioRepository.findAllByAtivoTrue(any(Pageable.class))).thenReturn(Page.empty());
        when(contaClient.listarContas()).thenReturn(List.of());

        // Act
        Page<DadosListagemUsuario> result = usuarioService.listarUsuariosComSaldo(Pageable.unpaged());

        // Assert
        assertEquals(0, result.getTotalElements());
    }
}