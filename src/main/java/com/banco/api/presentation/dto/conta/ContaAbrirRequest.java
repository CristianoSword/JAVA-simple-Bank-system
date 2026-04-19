package com.banco.api.presentation.dto.conta;

import com.banco.api.domain.model.TipoConta;
import jakarta.validation.constraints.NotNull;

public record ContaAbrirRequest(
        @NotNull Long clienteId,
        @NotNull TipoConta tipo
) {
}
