package com.academia.api.controller;

import com.academia.api.dto.PlanoCreateRequest;
import com.academia.api.dto.PlanoResponse;
import com.academia.api.mapper.PlanoMapper;
import com.academia.api.service.PlanoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/planos")
public class PlanoController {

    private final PlanoService planoService;

    public PlanoController(PlanoService planoService) {
        this.planoService = planoService;
    }

    @PostMapping
    public PlanoResponse cadastrar(
            @Valid @RequestBody PlanoCreateRequest request) {
        var plano = PlanoMapper.toEntity(request);
        return PlanoMapper.toResponse(planoService.cadastrar(plano));
    }

    @GetMapping
    public List<PlanoResponse> listarTodos() {
        return planoService.listarTodos()
                .stream()
                .map(PlanoMapper::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanoResponse> buscarPorId(
            @PathVariable Long id) {
        return planoService.buscarPorId(id)
                .map(plano -> ResponseEntity.ok(
                        PlanoMapper.toResponse(plano)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public PlanoResponse atualizar(
            @PathVariable Long id,
            @Valid @RequestBody PlanoCreateRequest request) {
        var plano = PlanoMapper.toEntity(request);
        return PlanoMapper.toResponse(
                planoService.atualizar(id, plano));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        planoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
