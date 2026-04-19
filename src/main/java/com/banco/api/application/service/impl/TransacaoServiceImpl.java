package com.banco.api.application.service.impl;

import com.banco.api.application.service.TransacaoService;
import com.banco.api.application.mapper.TransacaoMapper;
import com.banco.api.domain.exception.ContaNotFoundException;
import com.banco.api.infrastructure.repository.ContaRepository;
import com.banco.api.infrastructure.repository.TransacaoRepository;
import com.banco.api.presentation.dto.transacao.TransacaoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransacaoServiceImpl implements TransacaoService {

    private final TransacaoRepository transacaoRepository;
    private final ContaRepository contaRepository;

    @Override
    public List<TransacaoResponse> extratoPorConta(Long contaId) {
        if (!contaRepository.existsById(contaId)) {
            log.warn("Tentativa de consultar extrato de conta inexistente: {}", contaId);
            throw new ContaNotFoundException("Conta não encontrada com ID: " + contaId);
        }

        log.info("Consultando extrato para a conta ID: {}", contaId);
        return transacaoRepository.findByContaIdOrderByDataHoraDesc(contaId).stream()
                .map(TransacaoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Page<TransacaoResponse> extratoPorConta(Long contaId, Pageable pageable) {
        if (!contaRepository.existsById(contaId)) {
            log.warn("Tentativa de consultar extrato de conta inexistente: {}", contaId);
            throw new ContaNotFoundException("Conta não encontrada com ID: " + contaId);
        }

        log.info("Consultando extrato paginado para a conta ID: {}", contaId);
        return transacaoRepository.findByContaIdOrderByDataHoraDesc(contaId, pageable)
                .map(TransacaoMapper::toResponse);
    }
}
