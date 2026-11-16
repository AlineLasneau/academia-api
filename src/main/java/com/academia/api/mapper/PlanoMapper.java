package com.academia.api.mapper;

import com.academia.api.dto.PlanoCreateRequest;
import com.academia.api.dto.PlanoResponse;
import com.academia.api.model.Plano;

public class PlanoMapper {

    public static Plano toEntity(PlanoCreateRequest request) {
        Plano plano = new Plano();
        plano.setNome(request.nome());
        plano.setDescricao(request.descricao());
        plano.setDuracaoMeses(request.duracaoMeses());
        plano.setValor(request.valor());
        return plano;
    }

    public static PlanoResponse toResponse(Plano plano) {
        return new PlanoResponse(
                plano.getId(),
                plano.getNome(),
                plano.getDescricao(),
                plano.getDuracaoMeses(),
                plano.getValor(),
                plano.getAtivo()
        );
    }
}