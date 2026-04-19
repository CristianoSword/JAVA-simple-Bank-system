package com.banco.api.presentation.dto.conta;

import com.banco.api.domain.model.TipoConta;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Objeto para requisição de abertura de conta")
public record ContaAbrirRequest(
        @Schema(description = "ID do cliente que será titular da conta", example = "1")
        @NotNull Long clienteId,

        @Schema(description = "Tipo da conta a ser aberta", example = "CORRENTE")
        @NotNull TipoConta tipo
) {
}
