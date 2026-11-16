package com.academia.api.controller;

import com.academia.api.dto.ProfessorCreateRequest;
import com.academia.api.dto.ProfessorResponse;
import com.academia.api.mapper.ProfessorMapper;
import com.academia.api.service.ProfessorService;

import jakarta.validation.Valid;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/professores")
public class ProfessorController {

    private final ProfessorService service;

    public ProfessorController(ProfessorService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ProfessorResponse> cadastrar(
            @Valid @RequestBody ProfessorCreateRequest request) {

        var professor = service.cadastrar(
                ProfessorMapper.toEntity(request)
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ProfessorMapper.toResponse(professor));
    }

    @GetMapping
    public List<ProfessorResponse> listar() {
        return service.listarTodos()
                .stream()
                .map(ProfessorMapper::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public ProfessorResponse buscar(@PathVariable Long id) {
        return ProfessorMapper.toResponse(service.buscar(id));
    }

    @PutMapping("/{id}")
    public ProfessorResponse atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProfessorCreateRequest request) {

        var professor = service.atualizar(
                id,
                ProfessorMapper.toEntity(request)
        );

        return ProfessorMapper.toResponse(professor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}