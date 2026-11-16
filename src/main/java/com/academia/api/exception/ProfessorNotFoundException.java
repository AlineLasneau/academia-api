package com.academia.api.exception;

public class ProfessorNotFoundException extends RuntimeException {

    public ProfessorNotFoundException() {
        super("Professor não encontrado.");
    }
}