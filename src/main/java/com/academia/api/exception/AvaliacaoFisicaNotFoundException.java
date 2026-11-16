package com.academia.api.exception;

public class AvaliacaoFisicaNotFoundException
        extends RuntimeException {

    public AvaliacaoFisicaNotFoundException() {
        super("Avaliação física não encontrada.");
    }
}