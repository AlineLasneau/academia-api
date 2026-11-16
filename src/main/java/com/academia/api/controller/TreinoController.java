package com.academia.api.controller;

import com.academia.api.dto.*;
import com.academia.api.mapper.TreinoMapper;
import com.academia.api.service.TreinoService;

import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/treinos")
public class TreinoController {

    private final TreinoService service;

    public TreinoController(TreinoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TreinoResponse> cadastrar(
            @Valid @RequestBody TreinoCreateRequest request) {
        var treino = service.cadastrar(TreinoMapper.toEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(TreinoMapper.toResponse(treino));
    }

    @GetMapping
    public List<TreinoResponse> listar() {
        return service.listarTodos().stream()
                .map(TreinoMapper::toResponse).toList();
    }

    @GetMapping("/{id}")
    public TreinoResponse buscar(@PathVariable Long id) {
        return TreinoMapper.toResponse(service.buscar(id));
    }

    @GetMapping("/aluno/{alunoId}")
    public List<TreinoResponse> listarPorAluno(
            @PathVariable Long alunoId) {
        return service.listarPorAluno(alunoId).stream()
                .map(TreinoMapper::toResponse).toList();
    }

    @PutMapping("/{id}")
    public TreinoResponse atualizar(
            @PathVariable Long id,
            @Valid @RequestBody TreinoCreateRequest request) {
        var treino = service.atualizar(
                id, TreinoMapper.toEntity(request));
        return TreinoMapper.toResponse(treino);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}