package br.com.gestao.financeira.service.transacao.dto;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public record DadosCambio(

        String result,

        @JsonProperty("base_code")
        String baseCode,

        @JsonProperty("conversion_rates")
        Map<String, BigDecimal> conversionRates
) {
}
