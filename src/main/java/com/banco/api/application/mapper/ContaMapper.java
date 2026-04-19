package com.banco.api.application.mapper;

import com.banco.api.domain.model.Conta;
import com.banco.api.presentation.dto.conta.ContaResponse;

public class ContaMapper {

    public static ContaResponse toResponse(Conta conta) {
        return new ContaResponse(
                conta.getId(),
                conta.getNumeroConta(),
                conta.getSaldo(),
                conta.getTipo().name(),
                conta.isAtiva(),
                conta.getCliente().getNome());
    }
}
