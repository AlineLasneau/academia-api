package com.academia.api.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record AvaliacaoFisicaCreateRequest(

        @NotNull(message = "O aluno é obrigatório")
        @Positive
        Long alunoId,

        @NotNull(message = "A data é obrigatória")
        @PastOrPresent(message = "A data não pode ser futura")
        LocalDate dataAvaliacao,

        @NotNull(message = "O peso é obrigatório")
        @DecimalMin("1.00")
        BigDecimal peso,

        @NotNull(message = "A altura é obrigatória")
        @DecimalMin("0.50")
        BigDecimal altura,

        @DecimalMin("0.00")
        @DecimalMax("100.00")
        BigDecimal percentualGordura,

        String observacoes

) {}