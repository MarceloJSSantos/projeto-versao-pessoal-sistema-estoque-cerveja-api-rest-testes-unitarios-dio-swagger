package com.marcelojssantos.dio.estoquecerveja.service;

import com.marcelojssantos.dio.estoquecerveja.builder.CervejaDTOBuilder;
import com.marcelojssantos.dio.estoquecerveja.dto.CervejaDTO;
import com.marcelojssantos.dio.estoquecerveja.entity.Cerveja;
import com.marcelojssantos.dio.estoquecerveja.exception.CervejaEstaRegistradaException;
import com.marcelojssantos.dio.estoquecerveja.exception.CervejaNaoEncontradaException;
import com.marcelojssantos.dio.estoquecerveja.mapper.CervejaMapper;
import com.marcelojssantos.dio.estoquecerveja.repository.CervejaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CervejaServiceTest {

    @InjectMocks
    private CervejaService cervejaService;

    @Mock
    private CervejaRepository cervejaRepository;

    private final CervejaMapper cervejaMapper = CervejaMapper.INSTANCE;

    @Test
    void quandoCervejaEInformadaEntaoElaDeveSerInserida() throws CervejaEstaRegistradaException {
        //given
        CervejaDTO cervejaDTOEsperada = CervejaDTOBuilder.builder().build().toCervejaDTO();
        Cerveja cervejaSalva =  cervejaMapper.toModel(cervejaDTOEsperada);

        //when
        when(cervejaRepository.findByNome(cervejaDTOEsperada.getNome())).thenReturn(Optional.empty());
        when((cervejaRepository.save(cervejaSalva))).thenReturn(cervejaSalva);

        //then
        CervejaDTO cervejaDTOCriado = cervejaService.insert(cervejaDTOEsperada);
        assertThat(cervejaDTOCriado.getId(), is(equalTo(cervejaDTOEsperada.getId())));
        assertThat(cervejaDTOCriado.getNome(), is(equalTo(cervejaDTOEsperada.getNome())));
        assertThat(cervejaDTOCriado.getQuantidade(), is(equalTo(cervejaDTOEsperada.getQuantidade())));
    }

    @Test
    void quandoCervejaInformadaJaExisteEntaoUmaExceptionDeveSerLancada() throws CervejaEstaRegistradaException{
        //given
        CervejaDTO cervejaDTOEsperada = CervejaDTOBuilder.builder().build().toCervejaDTO();
        Cerveja cervejaDuplicada =  cervejaMapper.toModel(cervejaDTOEsperada);

        //when
        when(cervejaRepository.findByNome(cervejaDTOEsperada.getNome())).thenReturn(Optional.of(cervejaDuplicada));

        //then
        Assertions.assertThrows(CervejaEstaRegistradaException.class,
                () -> cervejaService.insert(cervejaDTOEsperada));

    }


    @Test
    void quandoNomeValidoCervejaEInformadoEntaoUmaCervejaERetornada() throws CervejaNaoEncontradaException {
        //given
        CervejaDTO cervejaDTOEsperada = CervejaDTOBuilder.builder().build().toCervejaDTO();
        Cerveja cervejaEncontrada =  cervejaMapper.toModel(cervejaDTOEsperada);

        //when
        when(cervejaRepository.findByNome(cervejaEncontrada.getNome())).thenReturn(Optional.of(cervejaEncontrada));

        //then
        CervejaDTO cervejaDTOEncontrada = cervejaService.findByNome(cervejaDTOEsperada.getNome());
        assertThat(cervejaDTOEncontrada, is(equalTo(cervejaDTOEsperada)));
    }

    @Test
    void quandoNomeCervejaEInformadoNaoEEncontradoEntaoUmExceptionERetornado() throws CervejaNaoEncontradaException {
        //given
        CervejaDTO cervejaDTOEsperada = CervejaDTOBuilder.builder().build().toCervejaDTO();

        //when
        when(cervejaRepository.findByNome(cervejaDTOEsperada.getNome())).thenReturn(Optional.empty());

        //then
        Assertions.assertThrows(CervejaNaoEncontradaException.class,
                () -> cervejaService.findByNome(cervejaDTOEsperada.getNome()));
    }
}