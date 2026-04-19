package com.banco.api.application.service;

import com.banco.api.domain.model.Conta;
import com.banco.api.domain.model.TipoConta;

import java.math.BigDecimal;
import java.util.List;

public interface ContaService {
    Conta abrir(Long clienteId, TipoConta tipo);

    Conta buscarPorNumeroConta(String numero);

    List<Conta> listarPorCliente(Long clienteId);

    Conta depositar(String numeroConta, BigDecimal valor);

    Conta sacar(String numeroConta, BigDecimal valor);

    void transferir(String numeroContaOrigem, String numeroContaDestino, BigDecimal valor);

    void encerrar(String numeroConta);
}
