package com.marcelojssantos.dio.estoquecerveja.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EstoqueCervejaMaxQuantMenorZeroException extends Exception{
    public EstoqueCervejaMaxQuantMenorZeroException(Long id, int quantidadeParaAlterar) {
        super(String.format("A quantidade para alteração (%s) para cerveja com ID %s resulta em uma capacidade máxima de estoque negativa!",
                quantidadeParaAlterar, id));
    }
}