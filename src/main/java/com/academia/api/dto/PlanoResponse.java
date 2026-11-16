package com.academia.api.dto;

import java.math.BigDecimal;

public record PlanoResponse(
        Long id,
        String nome,
        String descricao,
        Integer duracaoMeses,
        BigDecimal valor,
        Boolean ativo
) {}