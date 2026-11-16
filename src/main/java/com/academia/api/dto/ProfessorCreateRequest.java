package com.academia.api.dto;

import jakarta.validation.constraints.*;

public record ProfessorCreateRequest(

        @NotBlank(message = "O nome é obrigatório")
        String nomeCompleto,

        @NotBlank(message = "O CPF é obrigatório")
        String cpf,

        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "E-mail inválido")
        String email,

        @NotBlank(message = "O telefone é obrigatório")
        String telefone,

        @NotBlank(message = "A especialidade é obrigatória")
        String especialidade

) {}