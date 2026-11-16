package com.academia.api.service;

import com.academia.api.model.Professor;
import com.academia.api.repository.ProfessorRepository;
import com.academia.api.exception.ProfessorNotFoundException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfessorService {

    private final ProfessorRepository repository;

    public ProfessorService(ProfessorRepository repository) {
        this.repository = repository;
    }

    public Professor cadastrar(Professor professor) {

        if (repository.existsByCpf(professor.getCpf())) {
            throw new DataIntegrityViolationException(
                    "CPF já cadastrado."
            );
        }

        if (repository.existsByEmail(professor.getEmail())) {
            throw new DataIntegrityViolationException(
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
            throw new DataIntegrityViolationException(
                    "CPF já cadastrado."
            );
        }

        if (repository.existsByEmailAndIdNot(novo.getEmail(), id)) {
            throw new DataIntegrityViolationException(
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
        repository.delete(buscar(id));
    }
}