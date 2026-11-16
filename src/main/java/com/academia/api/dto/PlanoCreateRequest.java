
package com.academia.api.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record PlanoCreateRequest(

        @NotBlank(message = "O nome do plano é obrigatório")
        String nome,

        String descricao,

        @NotNull(message = "A duração é obrigatória")
        @Positive(message = "A duração deve ser positiva")
        Integer duracaoMeses,

        @NotNull(message = "O valor é obrigatório")
        @DecimalMin("0.01")
        BigDecimal valor

) {}
