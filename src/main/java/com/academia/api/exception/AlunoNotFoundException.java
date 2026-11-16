package com.academia.api.exception;

public class AlunoNotFoundException extends RuntimeException {

    public AlunoNotFoundException() {
        super("Aluno não encontrado.");
    }
}