package br.com.gestao.financeira.api.service;

import br.com.gestao.financeira.api.config.ExchangerateApiProperties;
import br.com.gestao.financeira.api.dto.DadosCambio;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@Service
public class ConversaoMoedaService {

    private final RestTemplate restTemplate;
    private final ExchangerateApiProperties properties;

    public ConversaoMoedaService(RestTemplate restTemplate,
                                 ExchangerateApiProperties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

    public BigDecimal converter(BigDecimal valor, String moedaDestino) {
        String moeda = moedaDestino.toUpperCase();
        // BRL é o valor padrão
        String url = properties.getUrl() + "/BRL";

        try {
            DadosCambio response = restTemplate.getForObject(url, DadosCambio.class);
            System.out.println("RESPONSE: "+response);
            if (response == null || response.conversionRates() == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_GATEWAY,
                        "Resposta vazia do serviço de câmbio"
                );
            }

            BigDecimal taxa = response.conversionRates().get(moeda);

            System.out.println("TAXA: " +taxa);
            if (taxa == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Moeda não suportada: " + moeda
                );
            }

            return valor.multiply(taxa);

        } catch (org.springframework.web.client.ResourceAccessException ex) {
            String causa = ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().toString() : ex.toString();
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "Serviço de câmbio indisponível ao acessar " + url + " (" + causa + ")",
                    ex
            );

        } catch (org.springframework.web.client.RestClientResponseException ex) {
            // aqui você pega casos em que conectou e recebeu HTTP 4xx/5xx
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "Erro HTTP do serviço de câmbio: " + ex.getRawStatusCode() + " ao acessar " + url,
                    ex
            );
        }
    }
}