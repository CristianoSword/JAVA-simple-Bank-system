package com.banco.api.application.mapper;

import com.banco.api.domain.model.Cliente;
import com.banco.api.presentation.dto.cliente.ClienteRequest;
import com.banco.api.presentation.dto.cliente.ClienteResponse;

public class ClienteMapper {

    public static Cliente toEntity(ClienteRequest dto) {
        return Cliente.builder()
                .nome(dto.nome())
                .cpf(dto.cpf())
                .email(dto.email())
                .build();
    }

    public static ClienteResponse toResponse(Cliente entity) {
        return new ClienteResponse(
                entity.getId(),
                entity.getNome(),
                entity.getCpf(),
                entity.getEmail(),
                entity.getDataCadastro());
    }
}
