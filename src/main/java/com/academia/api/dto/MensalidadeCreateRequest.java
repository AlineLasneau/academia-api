
package com.academia.api.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record MensalidadeCreateRequest(

        @NotNull(message = "O aluno é obrigatório")
        @Positive(message = "O ID do aluno deve ser positivo")
        Long alunoId,

        @NotNull(message = "O plano é obrigatório")
        @Positive(message = "O ID do plano deve ser positivo")
        Long planoId,

        @NotNull(message = "O valor é obrigatório")
        @DecimalMin("0.01")
        BigDecimal valor,

        @NotNull(message = "A data de vencimento é obrigatória")
        LocalDate dataVencimento,

        LocalDate dataPagamento,

        @Pattern(
                regexp = "PENDENTE|PAGO|ATRASADO",
                message = "Status inválido"
        )
        String status

) {}
