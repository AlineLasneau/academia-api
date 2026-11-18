package com.academia.api.repository;

import com.academia.api.model.Mensalidade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MensalidadeRepository extends JpaRepository<Mensalidade, Long> {

    boolean existsByAlunoId(Long alunoId);

    boolean existsByPlanoId(Long planoId);
}
