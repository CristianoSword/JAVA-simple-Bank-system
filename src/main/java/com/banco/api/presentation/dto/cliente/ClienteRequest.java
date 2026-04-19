package com.banco.api.presentation.dto.cliente;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

public record ClienteRequest(
        @NotBlank String nome,
        @NotBlank @CPF String cpf,
        @NotBlank @Email String email
) {
}
