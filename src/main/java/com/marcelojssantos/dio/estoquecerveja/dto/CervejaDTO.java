package com.marcelojssantos.dio.estoquecerveja.dto;

import com.marcelojssantos.dio.estoquecerveja.enums.TipoCerveja;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CervejaDTO {

    private Long id;

    @NotNull
    @Size(min = 1, max = 200)
    private String nome;

    @NotNull
    @Size(min = 1, max = 200)
    private String marca;

    @NotNull
    @Max(500)
    private Integer quantMax;

    @NotNull
    @Max(100)
    private Integer quantidade;

    @Enumerated(EnumType.STRING)
    @NotNull
    private TipoCerveja tipo;
}
