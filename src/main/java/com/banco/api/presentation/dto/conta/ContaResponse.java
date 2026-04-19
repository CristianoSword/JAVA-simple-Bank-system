package com.banco.api.presentation.dto.conta;

import java.math.BigDecimal;

public record ContaResponse(
        Long id,
        String numeroConta,
        BigDecimal saldo,
        String tipo,
        boolean ativa,
        String nomeCliente
) {
}
