package br.com.gestao.financeira.service.usuario.service;

import br.com.gestao.financeira.service.usuario.domain.usuario.dto.DadosSaldoConta;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
public class ContaClient {

    private final String url;
    private final RestTemplate restTemplate;

    public ContaClient(RestTemplate restTemplate, @Value("${api.conta.url}") String url) {
        this.restTemplate = restTemplate;
        this.url = url;
    }

    public List<DadosSaldoConta> listarContas() {
        DadosSaldoConta[] response =
                restTemplate.getForObject(url, DadosSaldoConta[].class);

        return Arrays.asList(response);
    }
}
