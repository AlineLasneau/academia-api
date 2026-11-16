package com.academia.api.exception;

public class PlanoNotFoundException extends RuntimeException {

    public PlanoNotFoundException() {
        super("Plano não encontrado.");
    }
}