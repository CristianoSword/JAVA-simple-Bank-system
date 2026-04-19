package com.banco.api.presentation.dto.conta;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record OperacaoRequest(
        @NotNull @DecimalMin("0.01") BigDecimal valor
) {
}
