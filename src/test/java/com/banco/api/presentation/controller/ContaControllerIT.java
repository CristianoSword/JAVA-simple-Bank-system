package com.banco.api.presentation.controller;

import com.banco.api.domain.model.Cliente;
import com.banco.api.domain.model.Conta;
import com.banco.api.domain.model.TipoConta;
import com.banco.api.infrastructure.repository.ClienteRepository;
import com.banco.api.infrastructure.repository.ContaRepository;
import com.banco.api.presentation.dto.conta.ContaAbrirRequest;
import com.banco.api.presentation.dto.conta.ContaResponse;
import com.banco.api.presentation.dto.conta.OperacaoRequest;
import com.banco.api.presentation.dto.conta.TransferenciaRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ContaControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    private Long clienteId;

    @BeforeEach
    void setUp() {
        contaRepository.deleteAll();
        clienteRepository.deleteAll();

        Cliente cliente = Cliente.builder()
                .nome("José Silva")
                .cpf("11122233344")
                .email("jose@email.com")
                .ativo(true)
                .build();
        clienteId = clienteRepository.save(cliente).getId();
    }

    @Test
    @DisplayName("Deve abrir uma conta com sucesso")
    void deveAbrirContaComSucesso() {
        ContaAbrirRequest request = new ContaAbrirRequest(clienteId, TipoConta.CORRENTE);

        ResponseEntity<ContaResponse> response = restTemplate.postForEntity("/api/contas", request,
                ContaResponse.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("CORRENTE", response.getBody().tipo());
    }

    @Test
    @DisplayName("Deve retornar 404 ao tentar abrir conta para cliente inexistente")
    void deveRetornar404ClienteInexistente() {
        ContaAbrirRequest request = new ContaAbrirRequest(999L, TipoConta.CORRENTE);

        ResponseEntity<String> response = restTemplate.postForEntity("/api/contas", request, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("Deve retornar 422 ao tentar transferir sem saldo")
    void deveRetornar422TransferenciaSemSaldo() {
        Conta origem = Conta.builder()
                .cliente(clienteRepository.findById(clienteId).get())
                .numeroConta("111")
                .tipo(TipoConta.CORRENTE)
                .ativa(true)
                .saldo(BigDecimal.ZERO)
                .build();
        Conta destino = Conta.builder()
                .cliente(clienteRepository.findById(clienteId).get())
                .numeroConta("222")
                .tipo(TipoConta.CORRENTE)
                .ativa(true)
                .saldo(BigDecimal.ZERO)
                .build();
        contaRepository.save(origem);
        contaRepository.save(destino);

        TransferenciaRequest request = new TransferenciaRequest("111", "222", new BigDecimal("500.00"));

        ResponseEntity<String> response = restTemplate.postForEntity("/api/contas/transferir", request, String.class);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    }
}
