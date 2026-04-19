package com.banco.api.presentation.dto.conta;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransferenciaRequest(
        @NotBlank String numeroContaOrigem,
        @NotBlank String numeroContaDestino,
        @NotNull @DecimalMin("0.01") BigDecimal valor
) {
}
