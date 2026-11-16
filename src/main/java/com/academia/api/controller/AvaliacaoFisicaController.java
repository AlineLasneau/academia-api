package com.academia.api.controller;

import com.academia.api.dto.*;
import com.academia.api.mapper.AvaliacaoFisicaMapper;
import com.academia.api.service.AvaliacaoFisicaService;

import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/avaliacoes-fisicas")
public class AvaliacaoFisicaController {

    private final AvaliacaoFisicaService service;

    public AvaliacaoFisicaController(
            AvaliacaoFisicaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<AvaliacaoFisicaResponse> cadastrar(
            @Valid @RequestBody AvaliacaoFisicaCreateRequest request) {

        var avaliacao = service.cadastrar(
                AvaliacaoFisicaMapper.toEntity(request));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(AvaliacaoFisicaMapper.toResponse(avaliacao));
    }

    @GetMapping
    public List<AvaliacaoFisicaResponse> listar() {
        return service.listarTodos().stream()
                .map(AvaliacaoFisicaMapper::toResponse).toList();
    }

    @GetMapping("/{id}")
    public AvaliacaoFisicaResponse buscar(@PathVariable Long id) {
        return AvaliacaoFisicaMapper.toResponse(service.buscar(id));
    }

    @GetMapping("/aluno/{alunoId}")
    public List<AvaliacaoFisicaResponse> listarPorAluno(
            @PathVariable Long alunoId) {
        return service.listarPorAluno(alunoId).stream()
                .map(AvaliacaoFisicaMapper::toResponse).toList();
    }

    @PutMapping("/{id}")
    public AvaliacaoFisicaResponse atualizar(
            @PathVariable Long id,
            @Valid @RequestBody AvaliacaoFisicaCreateRequest request) {

        var avaliacao = service.atualizar(
                id, AvaliacaoFisicaMapper.toEntity(request));

        return AvaliacaoFisicaMapper.toResponse(avaliacao);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}