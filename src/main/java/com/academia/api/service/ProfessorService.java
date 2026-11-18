package com.academia.api.service;

import com.academia.api.model.Professor;
import com.academia.api.repository.ProfessorRepository;
import com.academia.api.repository.TreinoRepository;
import com.academia.api.exception.DadoDuplicadoException;
import com.academia.api.exception.ProfessorNotFoundException;
import com.academia.api.exception.RegistroVinculadoException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProfessorService {

    private final ProfessorRepository repository;
    private final TreinoRepository treinoRepository;

    public ProfessorService(
            ProfessorRepository repository,
            TreinoRepository treinoRepository) {
        this.repository = repository;
        this.treinoRepository = treinoRepository;
    }

    public Professor cadastrar(Professor professor) {

        if (repository.existsByCpf(professor.getCpf())) {
            throw new DadoDuplicadoException(
                    "CPF já cadastrado."
            );
        }

        if (repository.existsByEmail(professor.getEmail())) {
            throw new DadoDuplicadoException(
                    "E-mail já cadastrado."
            );
        }

        return repository.save(professor);
    }

    public List<Professor> listarTodos() {
        return repository.findAll();
    }

    public Professor buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(ProfessorNotFoundException::new);
    }

    public Professor atualizar(Long id, Professor novo) {

        Professor professor = buscar(id);

        if (repository.existsByCpfAndIdNot(novo.getCpf(), id)) {
            throw new DadoDuplicadoException(
                    "CPF já cadastrado."
            );
        }

        if (repository.existsByEmailAndIdNot(novo.getEmail(), id)) {
            throw new DadoDuplicadoException(
                    "E-mail já cadastrado."
            );
        }

        professor.setNomeCompleto(novo.getNomeCompleto());
        professor.setCpf(novo.getCpf());
        professor.setEmail(novo.getEmail());
        professor.setTelefone(novo.getTelefone());
        professor.setEspecialidade(novo.getEspecialidade());

        return repository.save(professor);
    }

    public void excluir(Long id) {
        Professor professor = buscar(id);

        if (treinoRepository.existsByProfessorId(id)) {
            throw new RegistroVinculadoException(
                    "O professor possui treinos vinculados "
                            + "e não pode ser excluído.");
        }

        repository.delete(professor);
    }
}