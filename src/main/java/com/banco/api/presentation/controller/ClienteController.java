package com.banco.api.presentation.controller;

import lombok.RequiredArgsConstructor;

import com.banco.api.application.mapper.ClienteMapper;
import com.banco.api.application.service.ClienteService;
import com.banco.api.domain.model.Cliente;
import com.banco.api.presentation.dto.cliente.ClienteRequest;
import com.banco.api.presentation.dto.cliente.ClienteResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
@Tag(name = "Clientes", description = "Endpoints para gerenciamento de clientes.")
public class ClienteController {

        private final ClienteService clienteService;

        @Operation(summary = "Cadastrar um novo cliente", description = "Cria um novo cliente no sistema. O CPF dever ser único.")
        @ApiResponses({
                        @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso"),
                        @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos"),
                        @ApiResponse(responseCode = "409", description = "CPF já cadastrado")
        })
        @PostMapping
        public ResponseEntity<ClienteResponse> cadastrar(
                        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados para cadastro") @Valid @RequestBody ClienteRequest request) {
                Cliente cliente = ClienteMapper.toEntity(request);
                Cliente salvo = clienteService.cadastrar(cliente);
                return ResponseEntity.status(HttpStatus.CREATED).body(ClienteMapper.toResponse(salvo));
        }

        @Operation(summary = "Buscar cliente por ID")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
                        @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
        })
        @GetMapping("/{id}")
        public ResponseEntity<ClienteResponse> buscarPorId(@PathVariable Long id) {
                Cliente cliente = clienteService.buscarPorId(id);
                return ResponseEntity.ok(ClienteMapper.toResponse(cliente));
        }

        @Operation(summary = "Buscar cliente por CPF")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
                        @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
        })
        @GetMapping("/cpf/{cpf}")
        public ResponseEntity<ClienteResponse> buscarPorCpf(@PathVariable String cpf) {
                Cliente cliente = clienteService.buscarPorCpf(cpf);
                return ResponseEntity.ok(ClienteMapper.toResponse(cliente));
        }

        @Operation(summary = "Listar todos os clientes", description = "Retorna uma lista paginada de clientes")
        @ApiResponse(responseCode = "200", description = "Retorna a página de clientes")
        @GetMapping
        public ResponseEntity<Page<ClienteResponse>> listarTodos(
                        @ParameterObject @PageableDefault(size = 10, sort = "nome") Pageable pageable) {

                Page<ClienteResponse> clientes = clienteService.listarTodos(pageable)
                                .map(ClienteMapper::toResponse);

                return ResponseEntity.ok(clientes);
        }

        @Operation(summary = "Atualizar os dados de um cliente existente")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso"),
                        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                        @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
        })
        @PutMapping("/{id}")
        public ResponseEntity<ClienteResponse> atualizar(
                        @PathVariable Long id,
                        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Novos dados atualizados") @Valid @RequestBody ClienteRequest request) {
                Cliente dados = ClienteMapper.toEntity(request);
                Cliente atualizado = clienteService.atualizar(id, dados);
                return ResponseEntity.ok(ClienteMapper.toResponse(atualizado));
        }
}
