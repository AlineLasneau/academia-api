package com.academia.api.mapper;

import com.academia.api.model.Treino;
import com.academia.api.model.Aluno;
import com.academia.api.model.Professor;
import com.academia.api.dto.TreinoCreateRequest;
import com.academia.api.dto.TreinoResponse;

public class TreinoMapper {

    public static Treino toEntity(TreinoCreateRequest request) {
        Treino treino = new Treino();

        Aluno aluno = new Aluno();
        aluno.setId(request.alunoId());

        Professor professor = new Professor();
        professor.setId(request.professorId());

        treino.setNome(request.nome());
        treino.setDescricao(request.descricao());
        treino.setAluno(aluno);
        treino.setProfessor(professor);

        return treino;
    }

    public static TreinoResponse toResponse(Treino treino) {
        return new TreinoResponse(
                treino.getId(),
                treino.getNome(),
                treino.getDescricao(),
                treino.getAluno().getId(),
                treino.getProfessor().getId(),
                treino.getDataCriacao(),
                treino.getAtivo()
        );
    }
}