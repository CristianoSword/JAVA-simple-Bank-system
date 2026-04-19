package com.banco.api.presentation.dto.cliente;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

@Schema(description = "Objeto de requisição para criação/atualização de um cliente")
public record ClienteRequest(
        @Schema(description = "Nome completo do cliente", example = "João da Silva")
        @NotBlank(message = "{cliente.nome.obrigatorio}") String nome,

        @Schema(description = "CPF válido do cliente", example = "123.456.789-00")
        @NotBlank(message = "{cliente.cpf.obrigatorio}") @CPF(message = "{cliente.cpf.invalido}") String cpf,

        @Schema(description = "E-mail válido do cliente", example = "joao.silva@email.com")
        @NotBlank(message = "{cliente.email.obrigatorio}") @Email(message = "{cliente.email.invalido}") String email
) {
}
