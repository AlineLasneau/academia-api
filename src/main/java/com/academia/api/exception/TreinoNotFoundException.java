package com.academia.api.exception;

public class TreinoNotFoundException extends RuntimeException {
    public TreinoNotFoundException() {
        super("Treino não encontrado.");
    }
}