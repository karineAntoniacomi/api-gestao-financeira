package br.com.gestao.financeira.service.usuario.controller;

import br.com.gestao.financeira.service.usuario.service.UsuarioImportacaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UsuarioImportacaoControllerTest {

    private UsuarioImportacaoController controller;
    private UsuarioImportacaoService service;

    @BeforeEach
    void setUp() {
        service = Mockito.mock(UsuarioImportacaoService.class);
        controller = new UsuarioImportacaoController(service);
    }

    @Test
    @DisplayName("Deveria retornar 200 ok se arquivo valido")
    void importar_cenario1() {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "usuarios.xlsx",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new byte[]{1, 2, 3});

        ResponseEntity<Void> response = controller.importar(file);

        assertEquals(ResponseEntity.ok().build(), response);
        verify(service, times(1)).importar(file);
    }

    @Test
    @DisplayName("Deveria retornar codigo 400 se arquivo vazio")
    void importar_cenario2() {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "empty.xlsx",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new byte[]{});

        ResponseEntity<Void> response = controller.importar(file);

        assertEquals(ResponseEntity.badRequest().build(), response);
        verify(service, never()).importar(file);
    }
}