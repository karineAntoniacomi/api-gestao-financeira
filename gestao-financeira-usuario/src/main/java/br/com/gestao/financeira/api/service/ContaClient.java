package br.com.gestao.financeira.api.service;

import br.com.gestao.financeira.api.domain.usuario.dto.DadosSaldoConta;
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

    public List<DadosSaldoConta> listarContas() {
        DadosSaldoConta[] response =
                restTemplate.getForObject(URL, DadosSaldoConta[].class);

        return Arrays.asList(response);
    }
}
