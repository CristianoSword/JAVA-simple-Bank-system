package com.banco.api.application.service;

import com.banco.api.domain.model.Cliente;

import java.util.List;

public interface ClienteService {
    Cliente cadastrar(Cliente cliente);

    Cliente buscarPorId(Long id);

    Cliente buscarPorCpf(String cpf);

    List<Cliente> listarTodos();

    Cliente atualizar(Long id, Cliente dados);
}
