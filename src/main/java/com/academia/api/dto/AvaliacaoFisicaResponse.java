package com.academia.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AvaliacaoFisicaResponse(
        Long id,
        Long alunoId,
        LocalDate dataAvaliacao,
        BigDecimal peso,
        BigDecimal altura,
        BigDecimal percentualGordura,
        String observacoes
) {}