package com.marcelojssantos.dio.estoquecerveja.exception;

public class CervejaNaoEncontradaException extends Exception {

    public CervejaNaoEncontradaException(String nomeCerveja) {
        super(String.format("Cerveja com o nome '%s' não encontrada no sistema.", nomeCerveja));
    }

    public CervejaNaoEncontradaException(Long idCerveja) {
        super(String.format("Cerveja com id '%s' não encontrada no sistema.", idCerveja));
    }
}
