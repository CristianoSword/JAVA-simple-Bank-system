package com.banco.api.presentation.dto.transacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransacaoResponse(
        Long id,
        String tipo,
        BigDecimal valor,
        LocalDateTime dataHora,
        String descricao
) {
}
