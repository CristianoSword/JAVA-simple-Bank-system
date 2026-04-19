package com.banco.api.presentation.dto.conta;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Schema(description = "Objeto genérico para operações de saque e depósito")
public record OperacaoRequest(
        @Schema(description = "Valor da operação", example = "150.00")
        @NotNull(message = "{operacao.valor.obrigatorio}") @DecimalMin(value = "0.01", message = "{operacao.valor.minimo}") BigDecimal valor
) {
}
