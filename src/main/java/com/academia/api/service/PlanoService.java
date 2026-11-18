package com.academia.api.service;

import com.academia.api.model.Plano;
import com.academia.api.repository.MensalidadeRepository;
import com.academia.api.repository.PlanoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.academia.api.exception.PlanoNotFoundException;
import com.academia.api.exception.RegistroVinculadoException;

import java.util.List;

@Service
@Transactional
public class PlanoService {

    private final PlanoRepository planoRepository;
    private final MensalidadeRepository mensalidadeRepository;

    public PlanoService(
            PlanoRepository planoRepository,
            MensalidadeRepository mensalidadeRepository) {
        this.planoRepository = planoRepository;
        this.mensalidadeRepository = mensalidadeRepository;
    }

    public Plano cadastrar(Plano plano) {
        return planoRepository.save(plano);
    }

    public List<Plano> listarTodos() {
        return planoRepository.findAll();
    }

    public Plano buscarPorId(Long id) {
        return planoRepository.findById(id)
                .orElseThrow(PlanoNotFoundException::new);
    }

    public Plano atualizar(Long id, Plano planoAtualizado) {
        Plano plano = planoRepository.findById(id)
                .orElseThrow(PlanoNotFoundException::new);

        plano.setNome(planoAtualizado.getNome());
        plano.setDescricao(planoAtualizado.getDescricao());
        plano.setDuracaoMeses(planoAtualizado.getDuracaoMeses());
        plano.setValor(planoAtualizado.getValor());

        return planoRepository.save(plano);
    }

    public void excluir(Long id) {
        if (!planoRepository.existsById(id)) {
            throw new PlanoNotFoundException();
        }

        if (mensalidadeRepository.existsByPlanoId(id)) {
            throw new RegistroVinculadoException(
                    "O plano possui mensalidades vinculadas "
                            + "e não pode ser excluído.");
        }

        planoRepository.deleteById(id);
    }
}