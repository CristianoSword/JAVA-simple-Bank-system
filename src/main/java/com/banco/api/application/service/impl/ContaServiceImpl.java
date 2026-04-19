package com.banco.api.application.service.impl;

import com.banco.api.application.service.ClienteService;
import com.banco.api.application.service.ContaService;
import com.banco.api.domain.exception.ContaInativaException;
import com.banco.api.domain.exception.ContaNotFoundException;
import com.banco.api.domain.exception.SaldoInsuficienteException;
import com.banco.api.domain.model.*;
import com.banco.api.infrastructure.repository.ContaRepository;
import com.banco.api.infrastructure.repository.TransacaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContaServiceImpl implements ContaService {

    private final ContaRepository contaRepository;
    private final ClienteService clienteService;
    private final TransacaoRepository transacaoRepository;

    @Override
    @Transactional
    public Conta abrir(Long clienteId, TipoConta tipo) {
        log.info("Abrindo nova conta do tipo {} para o cliente ID: {}", tipo, clienteId);
        Cliente cliente = clienteService.buscarPorId(clienteId);

        String numeroConta = UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Conta novaConta = Conta.builder()
                .cliente(cliente)
                .numeroConta(numeroConta)
                .tipo(tipo)
                .ativa(true)
                .saldo(BigDecimal.ZERO)
                .build();

        return contaRepository.save(novaConta);
    }

    @Override
    public Conta buscarPorNumeroConta(String numero) {
        return contaRepository.findByNumeroConta(numero)
                .orElseThrow(() -> new ContaNotFoundException("Conta não encontrada: " + numero));
    }

    @Override
    public List<Conta> listarPorCliente(Long clienteId) {
        return contaRepository.findByClienteId(clienteId);
    }

    @Override
    @Transactional
    public Conta depositar(String numeroConta, BigDecimal valor) {
        log.info("Iniciando depósito de {} na conta {}", valor, numeroConta);
        Conta conta = buscarPorNumeroConta(numeroConta);
        validarContaAtiva(conta);
        conta.depositar(valor);
        Conta salva = contaRepository.save(conta);

        registrarTransacao(salva, TipoTransacao.DEPOSITO, valor, "Depósito em dinheiro");

        return salva;
    }

    @Override
    @Transactional
    public Conta sacar(String numeroConta, BigDecimal valor) {
        log.info("Iniciando saque de {} na conta {}", valor, numeroConta);
        Conta conta = buscarPorNumeroConta(numeroConta);
        validarContaAtiva(conta);

        if (conta.getSaldo().compareTo(valor) < 0) {
            log.warn("Falha no saque: saldo insuficiente na conta {}. Saldo atual: {}", numeroConta, conta.getSaldo());
            throw new SaldoInsuficienteException("Saldo insuficiente para o saque solicitado.");
        }

        conta.sacar(valor);
        Conta salva = contaRepository.save(conta);

        registrarTransacao(salva, TipoTransacao.SAQUE, valor, "Saque em dinheiro");

        return salva;
    }

    @Override
    @Transactional
    public void transferir(String numeroContaOrigem, String numeroContaDestino, BigDecimal valor) {
        log.info("Iniciando transferência de {} da conta {} para a conta {}", valor, numeroContaOrigem,
                numeroContaDestino);
        Conta origem = buscarPorNumeroConta(numeroContaOrigem);
        Conta destino = buscarPorNumeroConta(numeroContaDestino);

        validarContaAtiva(origem);
        validarContaAtiva(destino);

        if (origem.getSaldo().compareTo(valor) < 0) {
            log.warn("Falha na transferência: saldo insuficiente na conta de origem {}. Saldo atual: {}",
                    numeroContaOrigem, origem.getSaldo());
            throw new SaldoInsuficienteException("Saldo insuficiente na conta de origem para a transferência.");
        }

        origem.sacar(valor);
        destino.depositar(valor);

        contaRepository.save(origem);
        contaRepository.save(destino);

        registrarTransacao(origem, TipoTransacao.TRANSFERENCIA_SAIDA, valor,
                "Transferência para conta " + numeroContaDestino);
        registrarTransacao(destino, TipoTransacao.TRANSFERENCIA_ENTRADA, valor,
                "Transferência recebida da conta " + numeroContaOrigem);
    }

    @Override
    @Transactional
    public void encerrar(String numeroConta) {
        log.info("Encerrando a conta {}", numeroConta);
        Conta conta = buscarPorNumeroConta(numeroConta);
        conta.setAtiva(false);
        contaRepository.save(conta);
    }

    private void validarContaAtiva(Conta conta) {
        if (!conta.isAtiva()) {
            throw new ContaInativaException("Operação não permitida: conta inativa.");
        }
    }

    private void registrarTransacao(Conta conta, TipoTransacao tipo, BigDecimal valor, String descricao) {
        Transacao transacao = Transacao.builder()
                .conta(conta)
                .tipo(tipo)
                .valor(valor)
                .descricao(descricao)
                .build();
        transacaoRepository.save(transacao);
    }
}
