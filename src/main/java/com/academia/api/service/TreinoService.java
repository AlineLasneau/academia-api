package com.academia.api.service;

import com.academia.api.model.*;
import com.academia.api.repository.*;
import com.academia.api.exception.TreinoNotFoundException;
import com.academia.api.exception.AlunoNotFoundException;
import com.academia.api.exception.ProfessorNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class TreinoService {

    private final TreinoRepository treinoRepository;
    private final AlunoRepository alunoRepository;
    private final ProfessorRepository professorRepository;

    public TreinoService(
            TreinoRepository treinoRepository,
            AlunoRepository alunoRepository,
            ProfessorRepository professorRepository) {
        this.treinoRepository = treinoRepository;
        this.alunoRepository = alunoRepository;
        this.professorRepository = professorRepository;
    }

    public Treino cadastrar(Treino treino) {
        Long alunoId = treino.getAluno().getId();
        Long professorId = treino.getProfessor().getId();

        treino.setAluno(alunoRepository.findById(alunoId)
                .orElseThrow(AlunoNotFoundException::new));

        treino.setProfessor(professorRepository.findById(professorId)
                .orElseThrow(ProfessorNotFoundException::new));

        return treinoRepository.save(treino);
    }

    public List<Treino> listarTodos() {
        return treinoRepository.findAll();
    }

    public List<Treino> listarPorAluno(Long alunoId) {
        if (!alunoRepository.existsById(alunoId)) {
            throw new AlunoNotFoundException();
        }
        return treinoRepository.findByAlunoId(alunoId);
    }

    public Treino buscar(Long id) {
        return treinoRepository.findById(id)
                .orElseThrow(TreinoNotFoundException::new);
    }

    public Treino atualizar(Long id, Treino novo) {
        Treino treino = buscar(id);

        Long alunoId = novo.getAluno().getId();
        Long professorId = novo.getProfessor().getId();

        treino.setAluno(alunoRepository.findById(alunoId)
                .orElseThrow(AlunoNotFoundException::new));

        treino.setProfessor(professorRepository.findById(professorId)
                .orElseThrow(ProfessorNotFoundException::new));

        treino.setNome(novo.getNome());
        treino.setDescricao(novo.getDescricao());

        return treinoRepository.save(treino);
    }

    public void excluir(Long id) {
        treinoRepository.delete(buscar(id));
    }
}