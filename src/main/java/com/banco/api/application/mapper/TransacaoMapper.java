package com.banco.api.application.mapper;

import com.banco.api.domain.model.Transacao;
import com.banco.api.presentation.dto.transacao.TransacaoResponse;

public class TransacaoMapper {

    public static TransacaoResponse toResponse(Transacao t) {
        return new TransacaoResponse(
                t.getId(),
                t.getTipo().name(),
                t.getValor(),
                t.getDataHora(),
                t.getDescricao());
    }
}
