package com.marcelojssantos.dio.estoquecerveja.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CervejaEstaRegistradaException extends Exception{
    public CervejaEstaRegistradaException(String nomeCerveja) {
        super(String.format("Cerveja com nome '%s' já cadastrada no sistema.", nomeCerveja));
    }
}