package com.banco.api.presentation.dto.cliente;

import java.time.LocalDate;

public record ClienteResponse(
        Long id,
        String nome,
        String cpf,
        String email,
        LocalDate dataCadastro
) {
}
