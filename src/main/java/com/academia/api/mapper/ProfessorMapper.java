package com.academia.api.mapper;

import com.academia.api.dto.ProfessorCreateRequest;
import com.academia.api.dto.ProfessorResponse;
import com.academia.api.model.Professor;

public class ProfessorMapper {

    public static Professor toEntity(ProfessorCreateRequest request) {

        Professor professor = new Professor();

        professor.setNomeCompleto(request.nomeCompleto());
        professor.setCpf(request.cpf());
        professor.setEmail(request.email());
        professor.setTelefone(request.telefone());
        professor.setEspecialidade(request.especialidade());

        return professor;
    }

    public static ProfessorResponse toResponse(Professor professor) {

        return new ProfessorResponse(
                professor.getId(),
                professor.getNomeCompleto(),
                professor.getCpf(),
                professor.getEmail(),
                professor.getTelefone(),
                professor.getEspecialidade(),
                professor.getAtivo()
        );
    }
}