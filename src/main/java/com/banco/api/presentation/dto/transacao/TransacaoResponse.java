package com.banco.api.presentation.dto.transacao;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Resposta com os detalhes de uma transação do extrato")
public record TransacaoResponse(
        @Schema(description = "ID interno da transação", example = "100")
        Long id,

        @Schema(description = "Tipo da transação", example = "DEPOSITO")
        String tipo,

        @Schema(description = "Valor movimentado na transação", example = "500.00")
        BigDecimal valor,

        @Schema(description = "Data e hora exata da transação", example = "2023-10-15T10:30:00")
        LocalDateTime dataHora,

        @Schema(description = "Descrição formatada da operação", example = "Depósito em dinheiro")
        String descricao
) {
}
