package com.banco.api.application.service;

import com.banco.api.domain.model.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClienteService {
    Cliente cadastrar(Cliente cliente);

    Cliente buscarPorId(Long id);

    Cliente buscarPorCpf(String cpf);

    Page<Cliente> listarTodos(Pageable pageable);

    Cliente atualizar(Long id, Cliente dadosAtualizados);
}
