package com.banco.api.presentation.controller;

import com.banco.api.application.mapper.ClienteMapper;
import com.banco.api.application.service.ClienteService;
import com.banco.api.domain.model.Cliente;
import com.banco.api.presentation.dto.cliente.ClienteRequest;
import com.banco.api.presentation.dto.cliente.ClienteResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    public ResponseEntity<ClienteResponse> cadastrar(@Valid @RequestBody ClienteRequest request) {
        Cliente cliente = ClienteMapper.toEntity(request);
        Cliente salvo = clienteService.cadastrar(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(ClienteMapper.toResponse(salvo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> buscarPorId(@PathVariable Long id) {
        Cliente cliente = clienteService.buscarPorId(id);
        return ResponseEntity.ok(ClienteMapper.toResponse(cliente));
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<ClienteResponse> buscarPorCpf(@PathVariable String cpf) {
        Cliente cliente = clienteService.buscarPorCpf(cpf);
        return ResponseEntity.ok(ClienteMapper.toResponse(cliente));
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponse>> listarTodos() {
        List<ClienteResponse> clientes = clienteService.listarTodos().stream()
                .map(ClienteMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(clientes);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> atualizar(@PathVariable Long id,
            @Valid @RequestBody ClienteRequest request) {
        Cliente dados = ClienteMapper.toEntity(request);
        Cliente atualizado = clienteService.atualizar(id, dados);
        return ResponseEntity.ok(ClienteMapper.toResponse(atualizado));
    }
}
