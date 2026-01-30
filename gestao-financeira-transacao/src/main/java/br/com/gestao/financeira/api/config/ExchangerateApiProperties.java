package br.com.gestao.financeira.api.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "exchangerateapi.cambio")
@Getter
@Setter
public class ExchangerateApiProperties {
    private String url;
}
