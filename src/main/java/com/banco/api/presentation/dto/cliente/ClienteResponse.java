package com.banco.api.presentation.dto.cliente;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(description = "Resposta com os dados do cliente")
public record ClienteResponse(
        @Schema(description = "ID do cliente no sistema", example = "1")
        Long id,

        @Schema(description = "Nome completo do cliente", example = "João da Silva")
        String nome,

        @Schema(description = "CPF do cliente", example = "123.456.789-00")
        String cpf,

        @Schema(description = "E-mail do cliente", example = "joao.silva@email.com")
        String email,

        @Schema(description = "Data em que o cliente foi cadastrado", example = "2023-10-01")
        LocalDate dataCadastro
) {
}
