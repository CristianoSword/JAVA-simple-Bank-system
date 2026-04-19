package com.banco.api.application.service;

import com.banco.api.application.service.impl.TransacaoServiceImpl;
import com.banco.api.domain.exception.ContaNotFoundException;
import com.banco.api.domain.model.Transacao;
import com.banco.api.infrastructure.repository.ContaRepository;
import com.banco.api.infrastructure.repository.TransacaoRepository;
import com.banco.api.presentation.dto.transacao.TransacaoResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TransacaoServiceImplTest {

    @Mock
    private TransacaoRepository transacaoRepository;

    @Mock
    private ContaRepository contaRepository;

    @InjectMocks
    private TransacaoServiceImpl transacaoService;

    @Test
    @DisplayName("(1) deveRetornarExtratoDeContaExistente")
    void deveRetornarExtratoDeContaExistente() {
        Long contaId = 1L;
        given(contaRepository.existsById(contaId)).willReturn(true);
        given(transacaoRepository.findByContaIdOrderByDataHoraDesc(contaId))
                .willReturn(List.of(new Transacao(), new Transacao()));

        List<TransacaoResponse> result = transacaoService.extratoPorConta(contaId);

        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("(2) deveLancarExcecaoQuandoContaNaoEncontradaNoExtrato")
    void deveLancarExcecaoQuandoContaNaoEncontradaNoExtrato() {
        Long contaId = 1L;
        given(contaRepository.existsById(contaId)).willReturn(false);

        assertThrows(ContaNotFoundException.class, () -> transacaoService.extratoPorConta(contaId));
    }
}
