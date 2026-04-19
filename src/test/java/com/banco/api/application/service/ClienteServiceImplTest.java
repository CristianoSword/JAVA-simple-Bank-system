package com.banco.api.application.service;

import com.banco.api.application.service.impl.ClienteServiceImpl;
import com.banco.api.domain.exception.ClienteNotFoundException;
import com.banco.api.domain.exception.CpfJaCadastradoException;
import com.banco.api.domain.model.Cliente;
import com.banco.api.infrastructure.repository.ClienteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceImplTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    @Test
    @DisplayName("(1) deveCadastrarClienteComSucesso")
    void deveCadastrarClienteComSucesso() {
        // given
        Cliente cliente = Cliente.builder()
                .nome("João Silva")
                .cpf("12345678900")
                .email("joao@email.com")
                .build();
        given(clienteRepository.existsByCpf(cliente.getCpf())).willReturn(false);
        given(clienteRepository.save(any(Cliente.class))).willReturn(cliente);

        // when
        Cliente result = clienteService.cadastrar(cliente);

        // then
        assertNotNull(result);
        assertEquals(cliente.getNome(), result.getNome());
        verify(clienteRepository).save(cliente);
    }

    @Test
    @DisplayName("(2) deveLancarExcecaoQuandoCpfJaCadastrado")
    void deveLancarExcecaoQuandoCpfJaCadastrado() {
        // given
        Cliente cliente = Cliente.builder().cpf("12345678900").build();
        given(clienteRepository.existsByCpf(cliente.getCpf())).willReturn(true);

        // when / then
        assertThrows(CpfJaCadastradoException.class, () -> clienteService.cadastrar(cliente));
        verify(clienteRepository, never()).save(any());
    }

    @Test
    @DisplayName("(3) deveLancarExcecaoQuandoClienteNaoEncontradoPorId")
    void deveLancarExcecaoQuandoClienteNaoEncontradoPorId() {
        // given
        Long id = 1L;
        given(clienteRepository.findById(id)).willReturn(Optional.empty());

        // when / then
        assertThrows(ClienteNotFoundException.class, () -> clienteService.buscarPorId(id));
    }

    @Test
    @DisplayName("(4) deveRetornarClienteQuandoEncontradoPorCpf")
    void deveRetornarClienteQuandoEncontradoPorCpf() {
        // given
        String cpf = "12345678900";
        Cliente cliente = Cliente.builder().cpf(cpf).build();
        given(clienteRepository.findByCpf(cpf)).willReturn(Optional.of(cliente));

        // when
        Cliente result = clienteService.buscarPorCpf(cpf);

        // then
        assertNotNull(result);
        assertEquals(cpf, result.getCpf());
    }

    @Test
    @DisplayName("(5) deveListarTodosOsClientes")
    void deveListarTodosOsClientes() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        List<Cliente> clientes = List.of(new Cliente(), new Cliente());
        Page<Cliente> page = new PageImpl<>(clientes, pageable, clientes.size());
        given(clienteRepository.findAll(pageable)).willReturn(page);

        // when
        Page<Cliente> result = clienteService.listarTodos(pageable);

        // then
        assertEquals(2, result.getTotalElements());
        verify(clienteRepository).findAll(pageable);
    }
}
