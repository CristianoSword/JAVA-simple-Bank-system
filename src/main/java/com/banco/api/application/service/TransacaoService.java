package com.banco.api.application.service;

import com.banco.api.presentation.dto.transacao.TransacaoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TransacaoService {
    List<TransacaoResponse> extratoPorConta(Long contaId);

    Page<TransacaoResponse> extratoPorConta(Long contaId, Pageable pageable);
}
