
package com.academia.api.mapper;

import com.academia.api.dto.MensalidadeCreateRequest;
import com.academia.api.dto.MensalidadeResponse;
import com.academia.api.model.Mensalidade;
import com.academia.api.model.Aluno;
import com.academia.api.model.Plano;

public class MensalidadeMapper {

    public static Mensalidade toEntity(MensalidadeCreateRequest request) {
        Mensalidade mensalidade = new Mensalidade();

        mensalidade.setValor(request.valor());
        mensalidade.setDataVencimento(request.dataVencimento());
        mensalidade.setDataPagamento(request.dataPagamento());
        mensalidade.setStatus(
                request.status() == null ? "PENDENTE" : request.status()
        );

        Aluno aluno = new Aluno();
        aluno.setId(request.alunoId());

        Plano plano = new Plano();
        plano.setId(request.planoId());

        mensalidade.setAluno(aluno);
        mensalidade.setPlano(plano);

        return mensalidade;
    }

    public static MensalidadeResponse toResponse(Mensalidade mensalidade) {
        return new MensalidadeResponse(
                mensalidade.getId(),
                mensalidade.getAluno().getId(),
                mensalidade.getPlano().getId(),
                mensalidade.getValor(),
                mensalidade.getDataVencimento(),
                mensalidade.getDataPagamento(),
                mensalidade.getStatus()
        );
    }
}
