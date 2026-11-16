package com.academia.api.service;

import com.academia.api.model.Plano;
import com.academia.api.repository.PlanoRepository;
import org.springframework.stereotype.Service;
import com.academia.api.exception.PlanoNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class PlanoService {

    private final PlanoRepository planoRepository;

    public PlanoService(PlanoRepository planoRepository) {
        this.planoRepository = planoRepository;
    }

    public Plano cadastrar(Plano plano) {
        return planoRepository.save(plano);
    }

    public List<Plano> listarTodos() {
        return planoRepository.findAll();
    }

    public Optional<Plano> buscarPorId(Long id) {
        return planoRepository.findById(id);
    }

    public Plano atualizar(Long id, Plano planoAtualizado) {
        Plano plano = planoRepository.findById(id)
                .orElseThrow(PlanoNotFoundException::new);

        plano.setNome(planoAtualizado.getNome());
        plano.setDescricao(planoAtualizado.getDescricao());
        plano.setDuracaoMeses(planoAtualizado.getDuracaoMeses());
        plano.setValor(planoAtualizado.getValor());
        plano.setAtivo(planoAtualizado.getAtivo());

        return planoRepository.save(plano);
    }

    public void excluir(Long id) {
        if (!planoRepository.existsById(id)) {
            throw new PlanoNotFoundException();
        }

        planoRepository.deleteById(id);
    }
}