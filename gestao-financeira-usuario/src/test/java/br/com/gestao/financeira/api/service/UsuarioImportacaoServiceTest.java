package br.com.gestao.financeira.api.service;

import br.com.gestao.financeira.api.domain.usuario.UsuarioRepository;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioImportacaoServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioImportacaoService usuarioImportacaoService;

    @Test
    void importar_usuarios_sucesso_cenario1() throws Exception {
        // Arrange
        XSSFWorkbook workbook = new XSSFWorkbook();
        var sheet = workbook.createSheet();
        var headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("nome");
        headerRow.createCell(1).setCellValue("cpf");
        headerRow.createCell(2).setCellValue("cep");
        var dataRow = sheet.createRow(1);
        dataRow.createCell(0).setCellValue("João");
        dataRow.createCell(1).setCellValue("12345678901");
        dataRow.createCell(2).setCellValue("12345678");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);

        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getInputStream()).thenReturn(new ByteArrayInputStream(outputStream.toByteArray()));

        // Act
        usuarioImportacaoService.importar(mockFile);

        // Assert
        verify(usuarioRepository, times(1)).saveAll(anyList());
    }

    @Test
    @DisplayName("Deve lançar excesão se coluna obrigatoria não existir")
    void importar_usuarios_cenario2() throws Exception {
        // Arrange
        XSSFWorkbook workbook = new XSSFWorkbook();
        var sheet = workbook.createSheet();
        var headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("nome");
        // Missing 'cpf' and 'cep'

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);

        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getInputStream()).thenReturn(new ByteArrayInputStream(outputStream.toByteArray()));

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            usuarioImportacaoService.importar(mockFile);
        });

        assertEquals("Coluna obrigatória ausente: cpf", exception.getMessage());
    }

}