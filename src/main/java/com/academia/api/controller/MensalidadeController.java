
package com.academia.api.controller;

import com.academia.api.dto.MensalidadeCreateRequest;
import com.academia.api.dto.MensalidadeResponse;
import com.academia.api.mapper.MensalidadeMapper;
import com.academia.api.service.MensalidadeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mensalidades")
public class MensalidadeController {

    private final MensalidadeService mensalidadeService;

    public MensalidadeController(MensalidadeService mensalidadeService) {
        this.mensalidadeService = mensalidadeService;
    }

    @PostMapping
    public ResponseEntity<MensalidadeResponse> cadastrar(
            @Valid @RequestBody MensalidadeCreateRequest request) {
        var mensalidade = MensalidadeMapper.toEntity(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(MensalidadeMapper.toResponse(
                        mensalidadeService.cadastrar(mensalidade)));
    }

    @GetMapping
    public List<MensalidadeResponse> listarTodas() {
        return mensalidadeService.listarTodas()
                .stream()
                .map(MensalidadeMapper::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public MensalidadeResponse buscarPorId(@PathVariable Long id) {
        return MensalidadeMapper.toResponse(mensalidadeService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public MensalidadeResponse atualizar(
            @PathVariable Long id,
            @Valid @RequestBody MensalidadeCreateRequest request) {
        var mensalidade = MensalidadeMapper.toEntity(request);
        return MensalidadeMapper.toResponse(
                mensalidadeService.atualizar(id, mensalidade));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        mensalidadeService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
