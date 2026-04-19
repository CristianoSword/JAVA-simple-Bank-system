package com.banco.api.presentation.dto.conta;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Schema(description = "Objeto para requisição de transferência entre contas")
public record TransferenciaRequest(
        @Schema(description = "Número da conta que enviará o valor", example = "1A2B3C4D")
        @NotBlank String numeroContaOrigem,

        @Schema(description = "Número da conta que receberá o valor", example = "5E6F7G8H")
        @NotBlank String numeroContaDestino,

        @Schema(description = "Valor a ser transferido", example = "200.50")
        @NotNull @DecimalMin("0.01") BigDecimal valor
) {
}
