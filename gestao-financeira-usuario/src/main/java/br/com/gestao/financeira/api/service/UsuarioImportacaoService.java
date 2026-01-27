package br.com.gestao.financeira.api.service;

import br.com.gestao.financeira.api.domain.endereco.DadosEndereco;
import br.com.gestao.financeira.api.domain.endereco.Endereco;
import br.com.gestao.financeira.api.domain.usuario.DadosImportacaoUsuario;
import br.com.gestao.financeira.api.domain.usuario.Usuario;
import br.com.gestao.financeira.api.domain.usuario.UsuarioRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioImportacaoService {

    private final UsuarioRepository repository;

    public UsuarioImportacaoService(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void importar(MultipartFile file) {
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {

            Sheet sheet = workbook.getSheetAt(0);
            List<Usuario> usuarios = new ArrayList<>();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                DadosImportacaoUsuario dados = new DadosImportacaoUsuario(
                        getString(row, 0),
                        getString(row, 1),
                        getString(row, 2),
                        getString(row, 3),
                        getString(row, 4),
                        getString(row, 5),
                        getString(row, 6),
                        getString(row, 7),
                        getString(row, 8),
                        getString(row, 9),
                        getString(row, 10),
                        getString(row, 11)
                );

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
        return new DadosEndereco(
                dados.logradouro(),
                dados.bairro(),
                dados.cep(),
                dados.cidade(),
                dados.uf(),
                dados.complemento(),
                dados.numero()
        );
    }

    private String getString(Row row, int index) {
        Cell cell = row.getCell(index);
        return cell == null ? null : cell.toString().trim();
    }
}
