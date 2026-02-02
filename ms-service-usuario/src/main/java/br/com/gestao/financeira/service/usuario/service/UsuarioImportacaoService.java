package br.com.gestao.financeira.service.usuario.service;

import br.com.gestao.financeira.service.usuario.domain.usuario.dto.DadosImportacaoUsuario;
import br.com.gestao.financeira.service.usuario.domain.usuario.Usuario;
import br.com.gestao.financeira.service.usuario.domain.usuario.UsuarioRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UsuarioImportacaoService {

    private final DataFormatter formatter = new DataFormatter();

    private final UsuarioRepository repository;

    private final PasswordEncoder passwordEncoder;

    public UsuarioImportacaoService(
            UsuarioRepository repository,
            PasswordEncoder passwordEncoder
    ) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void importar(MultipartFile file) {
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {

            Sheet sheet = workbook.getSheetAt(0);

            Row headerRow = sheet.getRow(0);
            Map<String, Integer> colunas = mapearColunas(headerRow);

            validarColunasObrigatorias(colunas);

            List<Usuario> usuarios = new ArrayList<>();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                DadosImportacaoUsuario dados = new DadosImportacaoUsuario(
                        getString(row, colunas, "nome"),
                        getString(row, colunas, "email"),
                        getString(row, colunas, "cpf"),
                        getString(row, colunas, "telefone"),
                        getString(row, colunas, "profissao")
                );

                // Ignora campos vazios da planilha
                if (dados.nome() == null || dados.nome().isBlank() ||
                        dados.cpf() == null || dados.cpf().isBlank()) {
                    continue;
                }
                Usuario usuario = new Usuario();
                usuario.setNome(dados.nome());
                usuario.setEmail(dados.email());
                usuario.setCpf(dados.cpf());
                usuario.setTelefone(dados.telefone());
                usuario.setProfissao(dados.profissao());
                usuario.setAtivo(true);
                // Define senão padrão ao criar usuários importados da planilha
                usuario.setSenha(passwordEncoder.encode("1234"));

                usuarios.add(usuario);
            }

            repository.saveAll(usuarios);

        } catch (IOException e) {
            throw new RuntimeException("Erro ao processar arquivo Excel", e);
        }
    }

    private Map<String, Integer> mapearColunas(Row headerRow) {
        Map<String, Integer> colunas = new HashMap<>();

        for (Cell cell : headerRow) {
            String nomeColuna = cell.getStringCellValue()
                    .trim()
                    .toLowerCase()
                    .replaceAll("\\s+", "")     // remove espaços
                    .replaceAll("[^a-z]", "");  // remove acentos/símbolos

            colunas.put(nomeColuna, cell.getColumnIndex());
        }
        return colunas;
    }

    private void validarColunasObrigatorias(Map<String, Integer> colunas) {
        List<String> obrigatorias = List.of("nome", "cpf");

        for (String coluna : obrigatorias) {
            if (!colunas.containsKey(coluna)) {
                throw new RuntimeException("Coluna obrigatória ausente: " + coluna);
            }
        }
    }

    private String getString(Row row, Map<String, Integer> colunas, String nomeColuna) {
        Integer index = colunas.get(nomeColuna);
        if (index == null) return null;

        Cell cell = row.getCell(index);
        if (cell == null) return null;

        return formatter.formatCellValue(cell).trim();
    }


}
