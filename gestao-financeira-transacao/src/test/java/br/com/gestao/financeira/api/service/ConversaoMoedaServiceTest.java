package br.com.gestao.financeira.api.service;

import br.com.gestao.financeira.api.config.ExchangerateApiProperties;
import br.com.gestao.financeira.api.dto.DadosCambio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ConversaoMoedaServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ExchangerateApiProperties properties;

    private ConversaoMoedaService service;

    @BeforeEach
    void setUp() {
        service = new ConversaoMoedaService(restTemplate, properties);
    }

    @Test
    @DisplayName("Deveria multiplicar valor pela taxa quando resposta é valida")
    void converter_moeda_cenario1() {
        when(properties.getUrl()).thenReturn("https://example.invalid/latest");

        DadosCambio cambio = new DadosCambio(
                "success",
                "BRL",
                Map.of("USD", new BigDecimal("0.20"))
        );

        when(restTemplate.getForObject(eq("https://example.invalid/latest/BRL"), eq(DadosCambio.class)))
                .thenReturn(cambio);

        BigDecimal resultado = service.converter(new BigDecimal("10.00"), "usd");

        assertEquals(new BigDecimal("2.0000"), resultado.stripTrailingZeros().scale() >= 0 ? resultado : resultado);
        verify(restTemplate).getForObject("https://example.invalid/latest/BRL", DadosCambio.class);
    }

    @Test
    @DisplayName("Deveria retornar 502 quando resposta é nula")
    void converter_moeda_cenario2() {
        when(properties.getUrl()).thenReturn("https://example.invalid/latest");
        when(restTemplate.getForObject(eq("https://example.invalid/latest/BRL"), eq(DadosCambio.class)))
                .thenReturn(null);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.converter(new BigDecimal("10.00"), "USD"));

        assertEquals(HttpStatus.BAD_GATEWAY, ex.getStatusCode());
    }

    @Test
    @DisplayName("Deveria retornar 502 quando conversionRate é nulo")
    void converter_moeda_cenario3() {
        when(properties.getUrl()).thenReturn("https://example.invalid/latest");

        DadosCambio cambio = new DadosCambio("success", "BRL", null);

        when(restTemplate.getForObject(eq("https://example.invalid/latest/BRL"), eq(DadosCambio.class)))
                .thenReturn(cambio);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.converter(new BigDecimal("10.00"), "USD"));

        assertEquals(HttpStatus.BAD_GATEWAY, ex.getStatusCode());
    }

    @Test
    @DisplayName("Deveria retornar 400 quando Moeda nao é valida")
    void converter_moeda_cenario4() {
        when(properties.getUrl()).thenReturn("https://example.invalid/latest");

        DadosCambio cambio = new DadosCambio(
                "success",
                "BRL",
                Map.of("EUR", new BigDecimal("0.18"))
        );

        when(restTemplate.getForObject(eq("https://example.invalid/latest/BRL"), eq(DadosCambio.class)))
                .thenReturn(cambio);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.converter(new BigDecimal("10.00"), "USD"));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason() != null && ex.getReason().contains("Moeda não suportada: "));
    }

    @Test
    @DisplayName("Deveria retornar 503 quando Servico Indisponivel por rede")
    void converter_moeda_cenario5() {
        when(properties.getUrl()).thenReturn("https://example.invalid/latest");

        when(restTemplate.getForObject(eq("https://example.invalid/latest/BRL"), eq(DadosCambio.class)))
                .thenThrow(new ResourceAccessException("timeout"));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.converter(new BigDecimal("10.00"), "USD"));

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, ex.getStatusCode());
    }

    @Test
    @DisplayName("Deveria retornar 502 quando Servico Externo retorna ErroHttp")
    void converter_moeda_cenario6() {
        when(properties.getUrl()).thenReturn("https://example.invalid/latest");

        RestClientResponseException upstreamError =
                new RestClientResponseException(
                        "Bad Request",
                        400,
                        "Bad Request",
                        null,
                        "upstream".getBytes(StandardCharsets.UTF_8),
                        StandardCharsets.UTF_8
                );

        when(restTemplate.getForObject(eq("https://example.invalid/latest/BRL"), eq(DadosCambio.class)))
                .thenThrow(upstreamError);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.converter(new BigDecimal("10.00"), "USD"));

        assertEquals(HttpStatus.BAD_GATEWAY, ex.getStatusCode());
    }
}