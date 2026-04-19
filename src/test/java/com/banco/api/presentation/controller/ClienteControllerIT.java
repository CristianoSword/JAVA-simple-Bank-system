package com.banco.api.presentation.controller;

import com.banco.api.domain.model.Cliente;
import com.banco.api.infrastructure.repository.ClienteRepository;
import com.banco.api.presentation.dto.cliente.ClienteRequest;
import com.banco.api.presentation.dto.cliente.ClienteResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ClienteControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ClienteRepository clienteRepository;

    @BeforeEach
    void setUp() {
        clienteRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve cadastrar cliente com sucesso e retornar 201")
    void deveCadastrarClienteComSucesso() {
        ClienteRequest request = new ClienteRequest("João da Silva", "12345678909", "joao.silva@email.com");

        ResponseEntity<ClienteResponse> response = restTemplate.postForEntity("/api/clientes", request,
                ClienteResponse.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("João da Silva", response.getBody().nome());
    }

    @Test
    @DisplayName("Deve retornar 400 ao tentar cadastrar cliente com CPF invalido")
    void deveRetornar400AoCadastrarComDadosInvalidos() {
        ClienteRequest request = new ClienteRequest("", "123", "email-invalido");

        ResponseEntity<String> response = restTemplate.postForEntity("/api/clientes", request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Deve retornar a lista de clientes com código 200")
    void deveListarClientesComSucesso() {
        Cliente cliente = Cliente.builder()
                .nome("Maria Oliveira")
                .cpf("98765432100")
                .email("maria@email.com")
                .ativo(true)
                .build();
        clienteRepository.save(cliente);

        ResponseEntity<String> response = restTemplate.getForEntity("/api/clientes", String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}
