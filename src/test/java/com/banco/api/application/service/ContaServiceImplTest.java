package com.banco.api.application.service;

import com.banco.api.application.service.impl.ContaServiceImpl;
import com.banco.api.domain.exception.ContaInativaException;
import com.banco.api.domain.exception.SaldoInsuficienteException;
import com.banco.api.domain.model.Cliente;
import com.banco.api.domain.model.Conta;
import com.banco.api.domain.model.TipoConta;
import com.banco.api.domain.model.Transacao;
import com.banco.api.infrastructure.repository.ContaRepository;
import com.banco.api.infrastructure.repository.TransacaoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContaServiceImplTest {

    @Mock
    private ContaRepository contaRepository;

    @Mock
    private ClienteService clienteService;

    @Mock
    private TransacaoRepository transacaoRepository;

    @InjectMocks
    private ContaServiceImpl contaService;

    @Test
    @DisplayName("(1) deveAbrirContaParaClienteExistente")
    void deveAbrirContaParaClienteExistente() {
        Long clienteId = 1L;
        Cliente cliente = Cliente.builder().id(clienteId).build();
        given(clienteService.buscarPorId(clienteId)).willReturn(cliente);
        given(contaRepository.save(any(Conta.class))).willAnswer(invocation -> invocation.getArgument(0));

        Conta result = contaService.abrir(clienteId, TipoConta.CORRENTE);

        assertNotNull(result);
        assertEquals(cliente, result.getCliente());
        assertTrue(result.isAtiva());
        verify(contaRepository).save(any(Conta.class));
    }

    @Test
    @DisplayName("(3) deveDepositarValorNaConta")
    void deveDepositarValorNaConta() {
        String numero = "12345";
        BigDecimal valor = new BigDecimal("100.00");
        Conta conta = Conta.builder().numeroConta(numero).ativa(true).saldo(BigDecimal.ZERO).build();
        given(contaRepository.findByNumeroConta(numero)).willReturn(Optional.of(conta));
        given(contaRepository.save(any(Conta.class))).willReturn(conta);

        Conta result = contaService.depositar(numero, valor);

        assertEquals(valor, result.getSaldo());
        verify(contaRepository).save(conta);
        verify(transacaoRepository).save(any(Transacao.class)); // COMMIT 20 check
    }

    @Test
    @DisplayName("(5) deveSacarValorQuandoSaldoSuficiente")
    void deveSacarValorQuandoSaldoSuficiente() {
        String numero = "12345";
        BigDecimal saldoInicial = new BigDecimal("200.00");
        BigDecimal valorSaque = new BigDecimal("50.00");
        Conta conta = Conta.builder().numeroConta(numero).ativa(true).saldo(saldoInicial).build();
        given(contaRepository.findByNumeroConta(numero)).willReturn(Optional.of(conta));
        given(contaRepository.save(any(Conta.class))).willReturn(conta);

        Conta result = contaService.sacar(numero, valorSaque);

        assertEquals(new BigDecimal("150.00"), result.getSaldo());
        verify(contaRepository).save(conta);
        verify(transacaoRepository).save(any(Transacao.class)); // COMMIT 20 check
    }

    @Test
    @DisplayName("(1) deveRealizarTransferenciaComSucesso (COMMIT 16)")
    void deveRealizarTransferenciaComSucesso() {
        String numOrigem = "111";
        String numDestino = "222";
        BigDecimal valor = new BigDecimal("100.00");

        Conta origem = Conta.builder().numeroConta(numOrigem).ativa(true).saldo(new BigDecimal("200.00")).build();
        Conta destino = Conta.builder().numeroConta(numDestino).ativa(true).saldo(BigDecimal.ZERO).build();

        given(contaRepository.findByNumeroConta(numOrigem)).willReturn(Optional.of(origem));
        given(contaRepository.findByNumeroConta(numDestino)).willReturn(Optional.of(destino));

        contaService.transferir(numOrigem, numDestino, valor);

        assertEquals(new BigDecimal("100.00"), origem.getSaldo());
        assertEquals(new BigDecimal("100.00"), destino.getSaldo());
        verify(contaRepository, times(2)).save(any(Conta.class));
        verify(transacaoRepository, times(2)).save(any(Transacao.class)); // COMMIT 20 check
    }

    @Test
    @DisplayName("(2) deveLancarExcecaooperarEmContaOrigemInativaEmTransferencia")
    void deveLancarExcecaooperarEmContaOrigemInativaEmTransferencia() {
        String numOrigem = "111";
        String numDestino = "222";
        Conta origem = Conta.builder().numeroConta(numOrigem).ativa(false).build();
        Conta destino = Conta.builder().numeroConta(numDestino).ativa(true).build();

        given(contaRepository.findByNumeroConta(numOrigem)).willReturn(Optional.of(origem));
        given(contaRepository.findByNumeroConta(numDestino)).willReturn(Optional.of(destino));

        assertThrows(ContaInativaException.class, () -> contaService.transferir(numOrigem, numDestino, BigDecimal.TEN));
    }

    @Test
    @DisplayName("(4) deveLancarSaldoInsuficienteExceptionEmTransferencia")
    void deveLancarSaldoInsuficienteExceptionEmTransferencia() {
        String numOrigem = "111";
        String numDestino = "222";
        Conta origem = Conta.builder().numeroConta(numOrigem).ativa(true).saldo(BigDecimal.TEN).build();
        Conta destino = Conta.builder().numeroConta(numDestino).ativa(true).build();

        given(contaRepository.findByNumeroConta(numOrigem)).willReturn(Optional.of(origem));
        given(contaRepository.findByNumeroConta(numDestino)).willReturn(Optional.of(destino));

        assertThrows(SaldoInsuficienteException.class,
                () -> contaService.transferir(numOrigem, numDestino, new BigDecimal("50.00")));
    }
}
