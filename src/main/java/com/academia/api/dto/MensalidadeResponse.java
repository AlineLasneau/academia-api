package com.academia.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MensalidadeResponse(
        Long id,
        Long alunoId,
        Long planoId,
        BigDecimal valor,
        LocalDate dataVencimento,
        LocalDate dataPagamento,
        String status
) {}