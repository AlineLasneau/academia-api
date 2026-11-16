
package com.academia.api.service;

import com.academia.api.model.Mensalidade;
import com.academia.api.repository.MensalidadeRepository;
import com.academia.api.repository.AlunoRepository;
import com.academia.api.repository.PlanoRepository;
import com.academia.api.exception.AlunoNotFoundException;
import com.academia.api.exception.MensalidadeNotFoundException;
import com.academia.api.exception.PlanoNotFoundException;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MensalidadeService {

    private final MensalidadeRepository mensalidadeRepository;
    private final AlunoRepository alunoRepository;
    private final PlanoRepository planoRepository;

    public MensalidadeService(
            MensalidadeRepository mensalidadeRepository,
            AlunoRepository alunoRepository,
            PlanoRepository planoRepository) {

        this.mensalidadeRepository = mensalidadeRepository;
        this.alunoRepository = alunoRepository;
        this.planoRepository = planoRepository;
    }

    public Mensalidade cadastrar(Mensalidade mensalidade) {

        if (mensalidade.getAluno() == null ||
                mensalidade.getAluno().getId() == null) {
            throw new AlunoNotFoundException();
        }

        var aluno = alunoRepository.findById(mensalidade.getAluno().getId())
                .orElseThrow(AlunoNotFoundException::new);

        if (mensalidade.getPlano() == null ||
                mensalidade.getPlano().getId() == null) {
            throw new PlanoNotFoundException();
        }

        var plano = planoRepository.findById(mensalidade.getPlano().getId())
                .orElseThrow(PlanoNotFoundException::new);

        mensalidade.setAluno(aluno);
        mensalidade.setPlano(plano);

        return mensalidadeRepository.save(mensalidade);
    }

    public List<Mensalidade> listarTodas() {
        return mensalidadeRepository.findAll();
    }

    public Optional<Mensalidade> buscarPorId(Long id) {
        return mensalidadeRepository.findById(id);
    }

    public Mensalidade atualizar(Long id, Mensalidade mensalidadeAtualizada) {

        Mensalidade mensalidade = mensalidadeRepository.findById(id)
                .orElseThrow(MensalidadeNotFoundException::new);

        if (mensalidadeAtualizada.getAluno() == null ||
                mensalidadeAtualizada.getAluno().getId() == null) {
            throw new AlunoNotFoundException();
        }

        var aluno = alunoRepository.findById(
                mensalidadeAtualizada.getAluno().getId()
        ).orElseThrow(AlunoNotFoundException::new);

        if (mensalidadeAtualizada.getPlano() == null ||
                mensalidadeAtualizada.getPlano().getId() == null) {
            throw new PlanoNotFoundException();
        }

        var plano = planoRepository.findById(
                mensalidadeAtualizada.getPlano().getId()
        ).orElseThrow(PlanoNotFoundException::new);

        mensalidade.setAluno(aluno);
        mensalidade.setPlano(plano);
        mensalidade.setValor(mensalidadeAtualizada.getValor());
        mensalidade.setDataVencimento(mensalidadeAtualizada.getDataVencimento());
        mensalidade.setDataPagamento(mensalidadeAtualizada.getDataPagamento());
        mensalidade.setStatus(mensalidadeAtualizada.getStatus());

        return mensalidadeRepository.save(mensalidade);
    }

    public void excluir(Long id) {
        if (!mensalidadeRepository.existsById(id)) {
            throw new MensalidadeNotFoundException();
        }

        mensalidadeRepository.deleteById(id);
    }
}
