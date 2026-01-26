package br.com.gestao.financeira.api.domain.usuario;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SaldoUsuario {
    private final RestTemplate restTemplate;

    @Value("${mockapi.saldo.url}")
    private String baseUrl;

    public SaldoUsuario(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public SaldoContaDTO buscarSaldo(Long usuarioId) {
        return restTemplate.getForObject(
                baseUrl + "/saldo/" + usuarioId,
                SaldoContaDTO.class
        );
    }
}