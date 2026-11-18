package com.academia.api.service;

import com.academia.api.exception.AlunoNotFoundException;
import com.academia.api.exception.DadoDuplicadoException;
import com.academia.api.exception.RegistroVinculadoException;
import com.academia.api.model.Aluno;
import com.academia.api.repository.AlunoRepository;
import com.academia.api.repository.AvaliacaoFisicaRepository;
import com.academia.api.repository.MensalidadeRepository;
import com.academia.api.repository.TreinoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AlunoService {

    private final AlunoRepository alunoRepository;
    private final MensalidadeRepository mensalidadeRepository;
    private final TreinoRepository treinoRepository;
    private final AvaliacaoFisicaRepository avaliacaoFisicaRepository;

    public AlunoService(
            AlunoRepository alunoRepository,
            MensalidadeRepository mensalidadeRepository,
            TreinoRepository treinoRepository,
            AvaliacaoFisicaRepository avaliacaoFisicaRepository) {
        this.alunoRepository = alunoRepository;
        this.mensalidadeRepository = mensalidadeRepository;
        this.treinoRepository = treinoRepository;
        this.avaliacaoFisicaRepository = avaliacaoFisicaRepository;
    }

    public Aluno cadastrar(Aluno aluno) {
        if (alunoRepository.existsByCpf(aluno.getCpf())) {
            throw new DadoDuplicadoException("CPF já cadastrado.");
        }

        if (alunoRepository.existsByEmail(aluno.getEmail())) {
            throw new DadoDuplicadoException("E-mail já cadastrado.");
        }

        return alunoRepository.save(aluno);
    }

    public List<Aluno> listarTodos() {
        return alunoRepository.findAll();
    }

    public Aluno buscarPorId(Long id) {
        return alunoRepository.findById(id)
                .orElseThrow(AlunoNotFoundException::new);
    }

    public Aluno atualizar(Long id, Aluno alunoAtualizado) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(AlunoNotFoundException::new);

        if (alunoRepository.existsByCpfAndIdNot(
                alunoAtualizado.getCpf(), id)) {
            throw new DadoDuplicadoException("CPF já cadastrado.");
        }

        if (alunoRepository.existsByEmailAndIdNot(
                alunoAtualizado.getEmail(), id)) {
            throw new DadoDuplicadoException("E-mail já cadastrado.");
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

        if (mensalidadeRepository.existsByAlunoId(id)
                || treinoRepository.existsByAlunoId(id)
                || avaliacaoFisicaRepository.existsByAlunoId(id)) {
            throw new RegistroVinculadoException(
                    "O aluno possui mensalidades, treinos ou avaliações "
                            + "físicas vinculados e não pode ser excluído.");
        }

        alunoRepository.deleteById(id);
    }
}
