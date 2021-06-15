package com.marcelojssantos.dio.estoquecerveja.builder;

import com.marcelojssantos.dio.estoquecerveja.dto.CervejaDTO;
import com.marcelojssantos.dio.estoquecerveja.enums.TipoCerveja;
import lombok.Builder;

@Builder
public class CervejaDTOBuilder {

    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private String nome = "Brahma";

    @Builder.Default
    private String marca = "Ambev";

    @Builder.Default
    private int quantMax = 50;

    @Builder.Default
    private int quantidade = 10;

    @Builder.Default
    private TipoCerveja tipo = TipoCerveja.LAGER;

    public CervejaDTO toCervejaDTO() {
        return new CervejaDTO(id,
                nome,
                marca,
                quantMax,
                quantidade,
                tipo);
    }
}
