package com.marcelojssantos.dio.estoquecerveja.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuantidadeDTO {

    @NotNull
    @Max(100)
    private Integer quantidade;
}
