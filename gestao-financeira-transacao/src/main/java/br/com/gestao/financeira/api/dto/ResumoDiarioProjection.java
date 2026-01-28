package br.com.gestao.financeira.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ResumoDiarioProjection {
    LocalDate getData();
    BigDecimal getTotal();
}