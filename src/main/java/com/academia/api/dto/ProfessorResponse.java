package com.academia.api.dto;

public record ProfessorResponse(
        Long id,
        String nomeCompleto,
        String cpf,
        String email,
        String telefone,
        String especialidade,
        Boolean ativo
) {}