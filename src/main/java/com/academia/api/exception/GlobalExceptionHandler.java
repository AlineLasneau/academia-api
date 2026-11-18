package com.academia.api.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> tratarCorpoInvalido(
            HttpMessageNotReadableException exception) {
        return ResponseEntity.badRequest()
                .body(Map.of("erro",
                        "Corpo da requisição inválido: verifique o formato "
                                + "do JSON e os tipos dos campos."));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> tratarParametroInvalido(
            MethodArgumentTypeMismatchException exception) {
        return ResponseEntity.badRequest()
                .body(Map.of("erro",
                        "Valor inválido para o parâmetro '"
                                + exception.getName() + "'."));
    }

    @ExceptionHandler({
            DadoDuplicadoException.class,
            RegistroVinculadoException.class
    })
    public ResponseEntity<Map<String, String>> tratarConflito(
            RuntimeException exception) {
        return ResponseEntity.status(409)
                .body(Map.of("erro", exception.getMessage()));
    }

    // Rede de segurança para restrições do banco que escapem das
    // verificações dos services (ex.: duas requisições simultâneas).
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> tratarViolacaoIntegridade(
            DataIntegrityViolationException exception) {
        return ResponseEntity.status(409)
                .body(Map.of("erro",
                        "A operação viola uma restrição de integridade: "
                                + "dado duplicado ou registro vinculado."));
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