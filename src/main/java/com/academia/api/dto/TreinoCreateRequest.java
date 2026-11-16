package com.academia.api.dto;

import jakarta.validation.constraints.*;

public record TreinoCreateRequest(
        @NotBlank(message = "O nome é obrigatório")
        String nome,

        String descricao,

        @NotNull(message = "O aluno é obrigatório")
        @Positive
        Long alunoId,

        @NotNull(message = "O professor é obrigatório")
        @Positive
        Long professorId
) {}