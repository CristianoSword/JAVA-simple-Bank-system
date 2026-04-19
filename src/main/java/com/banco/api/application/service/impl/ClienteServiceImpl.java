package com.banco.api.application.service.impl;

import com.banco.api.application.service.ClienteService;
import com.banco.api.domain.exception.ClienteNotFoundException;
import com.banco.api.domain.exception.CpfJaCadastradoException;
import com.banco.api.domain.model.Cliente;
import com.banco.api.infrastructure.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    @Override
    @Transactional
    public Cliente cadastrar(Cliente cliente) {
        if (clienteRepository.existsByCpf(cliente.getCpf())) {
            throw new CpfJaCadastradoException("CPF já cadastrado: " + cliente.getCpf());
        }
        return clienteRepository.save(cliente);
    }

    @Override
    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException("Cliente não encontrado com ID: " + id));
    }

    @Override
    public Cliente buscarPorCpf(String cpf) {
        return clienteRepository.findByCpf(cpf)
                .orElseThrow(() -> new ClienteNotFoundException("Cliente não encontrado com CPF: " + cpf));
    }

    @Override
    public Page<Cliente> listarTodos(Pageable pageable) {
        return clienteRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public Cliente atualizar(Long id, Cliente dados) {
        Cliente clienteExistente = buscarPorId(id);
        clienteExistente.setNome(dados.getNome());
        clienteExistente.setEmail(dados.getEmail());
        // CPF geralmente não se altera em um sistema bancário simples, mas aqui
        // seguimos o padrão de atualização de dados.
        return clienteRepository.save(clienteExistente);
    }
}
