package com.banco.api.presentation.controller;

import com.banco.api.application.mapper.ContaMapper;
import com.banco.api.application.service.ContaService;
import com.banco.api.application.service.TransacaoService;
import com.banco.api.domain.model.Conta;
import com.banco.api.presentation.dto.conta.ContaAbrirRequest;
import com.banco.api.presentation.dto.conta.ContaResponse;
import com.banco.api.presentation.dto.conta.TransferenciaRequest;
import com.banco.api.presentation.dto.conta.OperacaoRequest;
import com.banco.api.presentation.dto.transacao.TransacaoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/contas")
@RequiredArgsConstructor
@Tag(name = "Contas", description = "Endpoints para gerenciamento de contas, operações e extratos.")
public class ContaController {

    private final ContaService contaService;
    private final TransacaoService transacaoService;

    @Operation(summary = "Abrir uma nova conta para um cliente")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Conta criada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente referenciado não encontrado")
    })
    @PostMapping
    public ResponseEntity<ContaResponse> abrir(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Tipo e cliente da conta") @Valid @RequestBody ContaAbrirRequest request) {
        Conta conta = contaService.abrir(request.clienteId(), request.tipo());
        return ResponseEntity.status(HttpStatus.CREATED).body(ContaMapper.toResponse(conta));
    }

    @Operation(summary = "Buscar uma conta pelo seu número", description = "Retorna os detalhes da conta identificada pelo número.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Conta encontrada"),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    })
    @GetMapping("/{numeroConta}")
    public ResponseEntity<ContaResponse> buscarPorNumero(@PathVariable String numeroConta) {
        Conta conta = contaService.buscarPorNumeroConta(numeroConta);
        return ResponseEntity.ok(ContaMapper.toResponse(conta));
    }

    @Operation(summary = "Listar todas as contas de um cliente")
    @ApiResponse(responseCode = "200", description = "Lista de contas retornada")
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<ContaResponse>> listarPorCliente(@PathVariable Long clienteId) {
        List<ContaResponse> contas = contaService.listarPorCliente(clienteId).stream()
                .map(ContaMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(contas);
    }

    @Operation(summary = "Realizar um depósito bancário")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Depósito efetuado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada"),
            @ApiResponse(responseCode = "422", description = "Conta inativa")
    })
    @PostMapping("/{numeroConta}/depositar")
    public ResponseEntity<ContaResponse> depositar(
            @PathVariable String numeroConta,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Valor a ser depositado") @Valid @RequestBody OperacaoRequest request) {
        Conta conta = contaService.depositar(numeroConta, request.valor());
        return ResponseEntity.ok(ContaMapper.toResponse(conta));
    }

    @Operation(summary = "Realizar um saque bancário")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Saque efetuado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada"),
            @ApiResponse(responseCode = "422", description = "Saldo insuficiente ou conta inativa")
    })
    @PostMapping("/{numeroConta}/sacar")
    public ResponseEntity<ContaResponse> sacar(
            @PathVariable String numeroConta,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Valor a ser sacado") @Valid @RequestBody OperacaoRequest request) {
        Conta conta = contaService.sacar(numeroConta, request.valor());
        return ResponseEntity.ok(ContaMapper.toResponse(conta));
    }

    @Operation(summary = "Realizar transferência entre contas", description = "Debita de uma conta de origem e credita na conta destino.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transferência efetuada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Uma das contas não foi encontrada"),
            @ApiResponse(responseCode = "422", description = "Saldo insuficiente na origem ou conta inativa")
    })
    @PostMapping("/transferir")
    public ResponseEntity<Map<String, ContaResponse>> transferir(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Contas de origem, destino e valor") @Valid @RequestBody TransferenciaRequest request) {
        contaService.transferir(request.numeroContaOrigem(), request.numeroContaDestino(), request.valor());

        Conta origem = contaService.buscarPorNumeroConta(request.numeroContaOrigem());
        Conta destino = contaService.buscarPorNumeroConta(request.numeroContaDestino());

        return ResponseEntity.ok(Map.of(
                "origem", ContaMapper.toResponse(origem),
                "destino", ContaMapper.toResponse(destino)));
    }

    @Operation(summary = "Obter extrato de uma conta, listando todas as transações de forma paginada.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Extrato gerado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    })
    @GetMapping("/{contaId}/extrato")
    public ResponseEntity<Page<TransacaoResponse>> extrato(
            @PathVariable Long contaId,
            @ParameterObject @PageableDefault(size = 20, sort = "dataHora", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<TransacaoResponse> extrato = transacaoService.extratoPorConta(contaId, pageable);
        return ResponseEntity.ok(extrato);
    }

    @Operation(summary = "Encerrar uma conta", description = "Marca a conta como desativada (inativa).")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Conta encerrada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    })
    @PatchMapping("/{numeroConta}/encerrar")
    public ResponseEntity<Void> encerrar(@PathVariable String numeroConta) {
        contaService.encerrar(numeroConta);
        return ResponseEntity.noContent().build();
    }
}
