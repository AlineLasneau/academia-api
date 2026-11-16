
package com.academia.api.controller;

import com.academia.api.dto.AlunoCreateRequest;
import com.academia.api.dto.AlunoResponse;
import com.academia.api.mapper.AlunoMapper;
import com.academia.api.service.AlunoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alunos")
public class AlunoController {

    private final AlunoService alunoService;

    public AlunoController(AlunoService alunoService) {
        this.alunoService = alunoService;
    }

    @PostMapping
    public AlunoResponse cadastrar(
            @Valid @RequestBody AlunoCreateRequest request) {
        var aluno = AlunoMapper.toEntity(request);
        return AlunoMapper.toResponse(alunoService.cadastrar(aluno));
    }

    @GetMapping
    public List<AlunoResponse> listarTodos() {
        return alunoService.listarTodos()
                .stream()
                .map(AlunoMapper::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlunoResponse> buscarPorId(
            @PathVariable Long id) {
        return alunoService.buscarPorId(id)
                .map(aluno -> ResponseEntity.ok(
                        AlunoMapper.toResponse(aluno)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public AlunoResponse atualizar(
            @PathVariable Long id,
            @Valid @RequestBody AlunoCreateRequest request) {
        var aluno = AlunoMapper.toEntity(request);
        return AlunoMapper.toResponse(
                alunoService.atualizar(id, aluno));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        alunoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
