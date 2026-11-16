
package com.academia.api.mapper;

import com.academia.api.dto.AlunoCreateRequest;
import com.academia.api.dto.AlunoResponse;
import com.academia.api.model.Aluno;

public class AlunoMapper {

    public static Aluno toEntity(AlunoCreateRequest request) {
        Aluno aluno = new Aluno();

        aluno.setNomeCompleto(request.nomeCompleto());
        aluno.setCpf(request.cpf());
        aluno.setEmail(request.email());
        aluno.setTelefone(request.telefone());
        aluno.setDataNascimento(request.dataNascimento());

        return aluno;
    }

    public static AlunoResponse toResponse(Aluno aluno) {
        return new AlunoResponse(
                aluno.getId(),
                aluno.getNomeCompleto(),
                aluno.getCpf(),
                aluno.getEmail(),
                aluno.getTelefone(),
                aluno.getDataNascimento(),
                aluno.getDataMatricula(),
                aluno.getAtivo()
        );
    }
}
