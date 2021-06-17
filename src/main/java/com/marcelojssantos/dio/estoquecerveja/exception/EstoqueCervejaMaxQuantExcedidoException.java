package com.marcelojssantos.dio.estoquecerveja.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EstoqueCervejaMaxQuantExcedidoException extends Exception{
    public EstoqueCervejaMaxQuantExcedidoException(Long id, int quantidadeParaAlterar) {
        super(String.format("A quantidade informado para alteração (%s) da cerveja com ID %s excede a capacidade máxima de estoque!",
                quantidadeParaAlterar, id));
    }
}