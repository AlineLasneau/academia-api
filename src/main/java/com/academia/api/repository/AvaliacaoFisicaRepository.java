package com.academia.api.repository;

import com.academia.api.model.AvaliacaoFisica;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AvaliacaoFisicaRepository
        extends JpaRepository<AvaliacaoFisica, Long> {

    List<AvaliacaoFisica> findByAlunoIdOrderByDataAvaliacaoDesc(
            Long alunoId);
}