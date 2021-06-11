package com.marcelojssantos.dio.estoquecerveja.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EstoqueCervejaExcedidoException extends Exception{
    public EstoqueCervejaExcedidoException(Long id, int quantidadeParaIncrementar) {
        super(String.format("O incremento informado para cervejas com ID '%s' excede a capacidade máxima de estoque: '%s'",
                id, quantidadeParaIncrementar));
    }
}