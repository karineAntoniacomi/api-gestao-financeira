package br.com.gestao.financeira.api.service;

import br.com.gestao.financeira.api.domain.usuario.dto.DadosListagemUsuario;
import br.com.gestao.financeira.api.domain.usuario.dto.DadosSaldoConta;
import br.com.gestao.financeira.api.domain.usuario.Usuario;
import br.com.gestao.financeira.api.domain.usuario.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final ContaClient contaClient;

    public UsuarioService(UsuarioRepository repository, ContaClient contaClient) {
        this.repository = repository;
        this.contaClient = contaClient;
    }

    public Page<DadosListagemUsuario> listarUsuariosComSaldo(Pageable paginacao) {

        // Busca usuários paginados
        Page<Usuario> usuariosPage = repository.findAllByAtivoTrue(paginacao);

        // Busca saldos da API Mock
        Map<Long, BigDecimal> saldoPorUsuario =
                contaClient.listarContas()
                        .stream()
                        .filter(DadosSaldoConta::ativa)
                        .collect(Collectors.toMap(
                                DadosSaldoConta::usuarioId,
                                DadosSaldoConta::saldo
                        ));

        // Converte Page<Usuario> → Page<DadosListagemUsuario>
        return usuariosPage.map(usuario ->
                new DadosListagemUsuario(
                        usuario.getId(),
                        usuario.getNome(),
                        usuario.getEmail(),
                        usuario.getCpf(),
                        saldoPorUsuario.getOrDefault(usuario.getId(), BigDecimal.ZERO)
                )
        );
    }
}