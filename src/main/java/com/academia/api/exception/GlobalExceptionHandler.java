package com.academia.api.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> tratarErroValidacao(
            MethodArgumentNotValidException exception) {

        Map<String, String> erros = new HashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(erro ->
                        erros.put(erro.getField(), erro.getDefaultMessage())
                );

        return ResponseEntity.badRequest().body(erros);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> tratarDuplicidade(
            DataIntegrityViolationException exception) {

        Map<String, String> erro = new HashMap<>();
        erro.put("erro", "CPF ou e-mail já cadastrado.");

        return ResponseEntity.status(409).body(erro);
    }

    @ExceptionHandler(AlunoNotFoundException.class)
    public ResponseEntity<Map<String, String>> tratarAlunoNaoEncontrado(
            AlunoNotFoundException exception) {

        Map<String, String> erro = new HashMap<>();
        erro.put("erro", exception.getMessage());

        return ResponseEntity.status(404).body(erro);
    }

    @ExceptionHandler(MensalidadeNotFoundException.class)
    public ResponseEntity<Map<String, String>> tratarMensalidadeNaoEncontrada(
            MensalidadeNotFoundException exception) {

        Map<String, String> erro = new HashMap<>();
        erro.put("erro", exception.getMessage());

        return ResponseEntity.status(404).body(erro);
    }

    @ExceptionHandler(PlanoNotFoundException.class)
    public ResponseEntity<Map<String, String>> tratarPlanoNaoEncontrado(
            PlanoNotFoundException exception) {

        Map<String, String> erro = new HashMap<>();
        erro.put("erro", exception.getMessage());

        return ResponseEntity.status(404).body(erro);
    }

    @ExceptionHandler(ProfessorNotFoundException.class)
    public ResponseEntity<Map<String, String>> professorNaoEncontrado(
            ProfessorNotFoundException e) {
        return ResponseEntity.status(404)
                .body(Map.of("erro", e.getMessage()));
    }

    @ExceptionHandler(TreinoNotFoundException.class)
    public ResponseEntity<Map<String, String>> treinoNaoEncontrado(
            TreinoNotFoundException e) {
        return ResponseEntity.status(404)
                .body(Map.of("erro", e.getMessage()));
    }

    @ExceptionHandler(AvaliacaoFisicaNotFoundException.class)
    public ResponseEntity<Map<String, String>> avaliacaoNaoEncontrada(
            AvaliacaoFisicaNotFoundException e) {
        return ResponseEntity.status(404)
                .body(Map.of("erro", e.getMessage()));
    }
}