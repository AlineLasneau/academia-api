package com.academia.api.service;

import com.academia.api.model.*;
import com.academia.api.repository.*;
import com.academia.api.exception.*;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AvaliacaoFisicaService {

    private final AvaliacaoFisicaRepository repository;
    private final AlunoRepository alunoRepository;

    public AvaliacaoFisicaService(
            AvaliacaoFisicaRepository repository,
            AlunoRepository alunoRepository) {
        this.repository = repository;
        this.alunoRepository = alunoRepository;
    }

    public AvaliacaoFisica cadastrar(AvaliacaoFisica avaliacao) {

        Long alunoId = avaliacao.getAluno().getId();

        avaliacao.setAluno(alunoRepository.findById(alunoId)
                .orElseThrow(AlunoNotFoundException::new));

        return repository.save(avaliacao);
    }

    public List<AvaliacaoFisica> listarTodos() {
        return repository.findAll();
    }

    public List<AvaliacaoFisica> listarPorAluno(Long alunoId) {
        if (!alunoRepository.existsById(alunoId)) {
            throw new AlunoNotFoundException();
        }

        return repository
                .findByAlunoIdOrderByDataAvaliacaoDesc(alunoId);
    }

    public AvaliacaoFisica buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(AvaliacaoFisicaNotFoundException::new);
    }

    public AvaliacaoFisica atualizar(
            Long id, AvaliacaoFisica nova) {

        AvaliacaoFisica avaliacao = buscar(id);

        Long alunoId = nova.getAluno().getId();

        avaliacao.setAluno(alunoRepository.findById(alunoId)
                .orElseThrow(AlunoNotFoundException::new));

        avaliacao.setDataAvaliacao(nova.getDataAvaliacao());
        avaliacao.setPeso(nova.getPeso());
        avaliacao.setAltura(nova.getAltura());
        avaliacao.setPercentualGordura(
                nova.getPercentualGordura());
        avaliacao.setObservacoes(nova.getObservacoes());

        return repository.save(avaliacao);
    }

    public void excluir(Long id) {
        repository.delete(buscar(id));
    }
}