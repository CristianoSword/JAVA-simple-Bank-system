package com.banco.api.application.service;

import com.banco.api.presentation.dto.transacao.TransacaoResponse;

import java.util.List;

public interface TransacaoService {
    List<TransacaoResponse> extratoPorConta(Long contaId);
}
