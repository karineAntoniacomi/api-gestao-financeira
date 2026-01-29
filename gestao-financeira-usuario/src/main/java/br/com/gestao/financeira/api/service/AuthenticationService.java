package br.com.gestao.financeira.api.service;

import br.com.gestao.financeira.api.domain.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements UserDetailsService {

    @Autowired
    private UsuarioRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails usuario = repository.findByEmail(username);
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuário não encontrado");
        }
        return usuario;
    }
}
