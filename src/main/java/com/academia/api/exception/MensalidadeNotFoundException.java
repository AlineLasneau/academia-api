package com.academia.api.exception;

public class MensalidadeNotFoundException extends RuntimeException {

    public MensalidadeNotFoundException() {
        super("Mensalidade não encontrada.");
    }
}