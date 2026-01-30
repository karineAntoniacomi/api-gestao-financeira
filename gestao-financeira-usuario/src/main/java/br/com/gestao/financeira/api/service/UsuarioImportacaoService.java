package br.com.gestao.financeira.api.service;

import br.com.gestao.financeira.api.domain.endereco.DadosEndereco;
import br.com.gestao.financeira.api.domain.endereco.Endereco;
import br.com.gestao.financeira.api.domain.usuario.dto.DadosImportacaoUsuario;
import br.com.gestao.financeira.api.domain.usuario.Usuario;
import br.com.gestao.financeira.api.domain.usuario.UsuarioRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

    public UsuarioImportacaoService(UsuarioRepository repository) {
        this.repository = repository;
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
                        getString(row, colunas, "profissao"),
                        getString(row, colunas, "logradouro"),
                        getString(row, colunas, "bairro"),
                        getString(row, colunas, "cep"),
                        getString(row, colunas, "cidade"),
                        getString(row, colunas, "uf"),
                        getString(row, colunas, "numero"),
                        getString(row, colunas, "complemento")
                );
                System.out.println(dados);
                if (dados.nome() == null || dados.nome().isEmpty()) {
                    continue;
                }

                DadosEndereco dadosEndereco = criarDadosEndereco(dados);
                Endereco endereco = new Endereco(dadosEndereco);

                Usuario usuario = new Usuario();
                usuario.setNome(dados.nome());
                usuario.setEmail(dados.email());
                usuario.setCpf(dados.cpf());
                usuario.setTelefone(dados.telefone());
                usuario.setProfissao(dados.profissao());
                usuario.setEndereco(endereco);
                usuario.setAtivo(true);

                usuarios.add(usuario);
            }

            repository.saveAll(usuarios);

        } catch (IOException e) {
            throw new RuntimeException("Erro ao processar arquivo Excel", e);
        }
    }

    private DadosEndereco criarDadosEndereco(DadosImportacaoUsuario dados) {

        String cep = dados.cep();

        if (cep == null || cep.isBlank()) {
            throw new RuntimeException("CEP obrigatório na planilha");
        }

        String cepLimpo = cep.replaceAll("\\D", "");

        if (cepLimpo.length() != 8) {
            throw new RuntimeException("CEP inválido: " + cep);
        }

        return new DadosEndereco(
                dados.logradouro(),
                dados.bairro(),
                cepLimpo,
                dados.cidade(),
                dados.uf(),
                dados.complemento(),
                dados.numero()
        );
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
        List<String> obrigatorias = List.of("nome", "cpf", "cep");

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
