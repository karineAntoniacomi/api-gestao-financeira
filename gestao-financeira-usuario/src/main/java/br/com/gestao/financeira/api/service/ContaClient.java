package br.com.gestao.financeira.api.service;

import br.com.gestao.financeira.api.domain.usuario.SaldoContaDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
public class ContaClient {

    private static final String URL =
            "http://69739102b5f46f8b5827af90.mockapi.io/conta";

    private final RestTemplate restTemplate;

    public ContaClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<SaldoContaDTO> listarContas() {
        SaldoContaDTO[] response =
                restTemplate.getForObject(URL, SaldoContaDTO[].class);

        return Arrays.asList(response);
    }
}
