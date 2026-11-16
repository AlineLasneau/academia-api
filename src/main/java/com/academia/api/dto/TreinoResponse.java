package com.academia.api.dto;

import java.time.LocalDate;

public record TreinoResponse(
        Long id,
        String nome,
        String descricao,
        Long alunoId,
        Long professorId,
        LocalDate dataCriacao,
        Boolean ativo
) {}