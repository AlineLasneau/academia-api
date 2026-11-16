package com.academia.api.service;

import com.academia.api.exception.AlunoNotFoundException;
import com.academia.api.model.Aluno;
import com.academia.api.repository.AlunoRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlunoService {

    private final AlunoRepository alunoRepository;

    public AlunoService(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    public Aluno cadastrar(Aluno aluno) {
        if (alunoRepository.existsByCpf(aluno.getCpf())) {
            throw new DataIntegrityViolationException("CPF já cadastrado.");
        }

        if (alunoRepository.existsByEmail(aluno.getEmail())) {
            throw new DataIntegrityViolationException("E-mail já cadastrado.");
        }

        return alunoRepository.save(aluno);
    }

    public List<Aluno> listarTodos() {
        return alunoRepository.findAll();
    }

    public Optional<Aluno> buscarPorId(Long id) {
        return alunoRepository.findById(id);
    }

    public Aluno atualizar(Long id, Aluno alunoAtualizado) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(AlunoNotFoundException::new);

        if (alunoRepository.existsByCpfAndIdNot(
                alunoAtualizado.getCpf(), id)) {
            throw new DataIntegrityViolationException("CPF já cadastrado.");
        }

        if (alunoRepository.existsByEmailAndIdNot(
                alunoAtualizado.getEmail(), id)) {
            throw new DataIntegrityViolationException("E-mail já cadastrado.");
        }

        aluno.setNomeCompleto(alunoAtualizado.getNomeCompleto());
        aluno.setCpf(alunoAtualizado.getCpf());
        aluno.setEmail(alunoAtualizado.getEmail());
        aluno.setTelefone(alunoAtualizado.getTelefone());
        aluno.setDataNascimento(alunoAtualizado.getDataNascimento());

        return alunoRepository.save(aluno);
    }

    public void excluir(Long id) {
        if (!alunoRepository.existsById(id)) {
            throw new AlunoNotFoundException();
        }

        alunoRepository.deleteById(id);
    }
}
