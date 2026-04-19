package com.banco.api.presentation.dto.conta;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Resposta com os dados da conta")
public record ContaResponse(
        @Schema(description = "ID interno da conta", example = "1")
        Long id,

        @Schema(description = "Número público da conta gerado automaticamente", example = "1A2B3C4D")
        String numeroConta,

        @Schema(description = "Saldo atual da conta", example = "1500.50")
        BigDecimal saldo,

        @Schema(description = "Tipo da conta", example = "CORRENTE")
        String tipo,

        @Schema(description = "Se a conta está ativa ou encerrada", example = "true")
        boolean ativa,

        @Schema(description = "Nome do cliente titular da conta", example = "João da Silva")
        String nomeCliente
) {
}
