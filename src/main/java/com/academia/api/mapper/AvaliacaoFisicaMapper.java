package com.academia.api.mapper;

import com.academia.api.model.AvaliacaoFisica;
import com.academia.api.model.Aluno;
import com.academia.api.dto.AvaliacaoFisicaCreateRequest;
import com.academia.api.dto.AvaliacaoFisicaResponse;

public class AvaliacaoFisicaMapper {

    public static AvaliacaoFisica toEntity(
            AvaliacaoFisicaCreateRequest request) {

        AvaliacaoFisica avaliacao = new AvaliacaoFisica();

        Aluno aluno = new Aluno();
        aluno.setId(request.alunoId());

        avaliacao.setAluno(aluno);
        avaliacao.setDataAvaliacao(request.dataAvaliacao());
        avaliacao.setPeso(request.peso());
        avaliacao.setAltura(request.altura());
        avaliacao.setPercentualGordura(
                request.percentualGordura());
        avaliacao.setObservacoes(request.observacoes());

        return avaliacao;
    }

    public static AvaliacaoFisicaResponse toResponse(
            AvaliacaoFisica avaliacao) {

        return new AvaliacaoFisicaResponse(
                avaliacao.getId(),
                avaliacao.getAluno().getId(),
                avaliacao.getDataAvaliacao(),
                avaliacao.getPeso(),
                avaliacao.getAltura(),
                avaliacao.getPercentualGordura(),
                avaliacao.getObservacoes()
        );
    }
}