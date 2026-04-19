package com.banco.api.presentation.exception;

import java.time.LocalDateTime;

public record ErroResponse(int status, String mensagem, LocalDateTime timestamp) {
}
