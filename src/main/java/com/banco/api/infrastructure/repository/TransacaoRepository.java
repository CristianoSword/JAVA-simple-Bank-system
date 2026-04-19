package com.banco.api.infrastructure.repository;

import com.banco.api.domain.model.Transacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
    List<Transacao> findByContaIdOrderByDataHoraDesc(Long contaId);
    // Nota: findByContaIdOrderByDataHoraDesc(Long contaId, Pageable pageable) será
    // adicionado no COMMIT 24.
}
