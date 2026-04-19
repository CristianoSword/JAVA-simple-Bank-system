package com.banco.api.application.mapper;

import com.banco.api.domain.model.Cliente;
import com.banco.api.presentation.dto.cliente.ClienteRequest;
import com.banco.api.presentation.dto.cliente.ClienteResponse;

public class ClienteMapper {

    public static ClienteResponse toResponse(Cliente cliente) {
        return new ClienteResponse(
                cliente.getId(),
                cliente.getNome(),
                cliente.getCpf(),
                cliente.getEmail(),
                cliente.isAtivo(),
                cliente.getDataCriacao());
    }

    public static Cliente toEntity(ClienteRequest request) {
        return Cliente.builder()
                .nome(request.nome())
                .cpf(request.cpf())
                .email(request.email())
                .build();
    }
}
