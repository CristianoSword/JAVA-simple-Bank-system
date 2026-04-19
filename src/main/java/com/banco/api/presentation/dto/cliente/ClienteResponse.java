package com.banco.api.presentation.dto.cliente;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Objeto de resposta com os dados do cliente")
public record ClienteResponse(
        @Schema(description = "ID do cliente", example = "1")
        Long id,

        @Schema(description = "Nome completo do cliente", example = "João da Silva")
        String nome,

        @Schema(description = "CPF do cliente (somente números)", example = "12345678909")
        String cpf,

        @Schema(description = "E-mail do cliente", example = "joao.silva@email.com")
        String email,

        @Schema(description = "Indica se o cliente está ativo", example = "true")
        boolean ativo,

        @Schema(description = "Data de cadastro do cliente", example = "2023-10-25T14:30:00")
        LocalDateTime dataCriacao
) {
}
