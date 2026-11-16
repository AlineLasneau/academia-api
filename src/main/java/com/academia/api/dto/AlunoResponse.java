package com.academia.api.dto;

import java.time.LocalDate;

public record AlunoResponse(
        Long id,
        String nomeCompleto,
        String cpf,
        String email,
        String telefone,
        LocalDate dataNascimento,
        LocalDate dataMatricula,
        Boolean ativo
) {}
