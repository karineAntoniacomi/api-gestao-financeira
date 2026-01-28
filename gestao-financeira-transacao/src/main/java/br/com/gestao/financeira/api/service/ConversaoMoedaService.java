package br.com.gestao.financeira.api.service;

import br.com.gestao.financeira.api.config.BrasilApiProperties;
import br.com.gestao.financeira.api.dto.DadosCambio;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@Service
public class ConversaoMoedaService {

    private final RestTemplate restTemplate;
    private final BrasilApiProperties properties;

    public ConversaoMoedaService(RestTemplate restTemplate,
                                 BrasilApiProperties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

    public BigDecimal converter(BigDecimal valor, String moedaDestino) {
        try {
            String url = properties.getUrl() + "/" + moedaDestino.toUpperCase();
            DadosCambio response =
                    restTemplate.getForObject(url, DadosCambio.class);

            if (response == null || response.rate() == null) {
                throw new RuntimeException("Erro ao obter taxa de câmbio");
            }
            return valor.multiply(response.rate());

        } catch (ResourceAccessException ex) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "Serviço de câmbio indisponível"
            );
        }
    }
}