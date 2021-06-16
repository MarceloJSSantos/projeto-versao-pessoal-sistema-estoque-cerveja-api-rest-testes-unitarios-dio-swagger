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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CervejaServiceTest {

    private final CervejaMapper cervejaMapper = CervejaMapper.INSTANCE;
    @InjectMocks
    private CervejaService cervejaService;
    @Mock
    private CervejaRepository cervejaRepository;

    @Test
    void quandoCervejaEInformadaEntaoElaDeveSerInserida() throws CervejaEstaRegistradaException {
        //given
        CervejaDTO cervejaDTOEsperada = CervejaDTOBuilder.builder().build().toCervejaDTO();
        Cerveja cervejaSalva = cervejaMapper.toModel(cervejaDTOEsperada);

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
    void quandoCervejaInformadaJaExisteEntaoExceptionERetornada() {
        //given
        CervejaDTO cervejaDTOEsperada = CervejaDTOBuilder.builder().build().toCervejaDTO();
        Cerveja cervejaDuplicada = cervejaMapper.toModel(cervejaDTOEsperada);

        //when
        when(cervejaRepository.findByNome(cervejaDTOEsperada.getNome())).thenReturn(Optional.of(cervejaDuplicada));

        //then
        Assertions.assertThrows(CervejaEstaRegistradaException.class,
                () -> cervejaService.insert(cervejaDTOEsperada));

    }


    @Test
    void quandoNomeValidoCervejaEInformadoEntaoCervejaERetornada() throws CervejaNaoEncontradaException {
        //given
        CervejaDTO cervejaDTOEsperada = CervejaDTOBuilder.builder().build().toCervejaDTO();
        Cerveja cervejaEncontrada = cervejaMapper.toModel(cervejaDTOEsperada);

        //when
        when(cervejaRepository.findByNome(cervejaEncontrada.getNome())).thenReturn(Optional.of(cervejaEncontrada));

        //then
        CervejaDTO cervejaDTOEncontrada = cervejaService.findByNome(cervejaDTOEsperada.getNome());
        assertThat(cervejaDTOEncontrada, is(equalTo(cervejaDTOEsperada)));
    }

    @Test
    void quandoNomeCervejaEInformadoNaoEEncontradoEntaoExceptionERetornado() {
        //given
        CervejaDTO cervejaDTOEsperada = CervejaDTOBuilder.builder().build().toCervejaDTO();

        //when
        when(cervejaRepository.findByNome(cervejaDTOEsperada.getNome())).thenReturn(Optional.empty());

        //then
        Assertions.assertThrows(CervejaNaoEncontradaException.class,
                () -> cervejaService.findByNome(cervejaDTOEsperada.getNome()));
    }

    @Test
    void quandoListaDeCervejasEChamadaTendoCervejasEntaoRetornaListaDeCerveja() {
        //given
        CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().toCervejaDTO();
        Cerveja cerveja = cervejaMapper.toModel(cervejaDTO);

        //when
        when(cervejaRepository.findAll()).thenReturn(Collections.singletonList(cerveja));

        //then
        List<CervejaDTO> listaCervejas = cervejaService.listAll();
        assertThat(listaCervejas, is(not(empty())));
        assertThat(listaCervejas.get(0), is(equalTo(cervejaDTO)));
    }

    @Test
    void quandoListaDeCervejasEChamadaNaoTendoCervejasEntaoRetornaListaVazia() {
        //when
        when(cervejaRepository.findAll()).thenReturn(Collections.emptyList());

        //then
        List<CervejaDTO> listaCervejas = cervejaService.listAll();
        assertThat(listaCervejas, is(empty()));
    }

    @Test
    void quandoExcluirCervejaComIdValidoEntaoCervejaEExcluida() throws CervejaNaoEncontradaException {
        //given
        CervejaDTO cervejaDTODeletadaEsperada = CervejaDTOBuilder.builder().build().toCervejaDTO();
        Cerveja cervejaDeletadaEsperada = cervejaMapper.toModel(cervejaDTODeletadaEsperada);

        //when
        when(cervejaRepository.findById(cervejaDTODeletadaEsperada.getId()))
                .thenReturn(Optional.of(cervejaDeletadaEsperada));
        doNothing().when(cervejaRepository).deleteById(cervejaDTODeletadaEsperada.getId());

        //then
        cervejaService.deleteById(cervejaDTODeletadaEsperada.getId());
        verify(cervejaRepository, times(1))
                .findById(cervejaDTODeletadaEsperada.getId());
        verify(cervejaRepository, times(1))
                .deleteById(cervejaDTODeletadaEsperada.getId());
    }

    @Test
    void quandoExcluirCervejaComIdNaoEEncontradoEntaoExceptionERetornado() {
        //given
        CervejaDTO cervejaDTODeletadaEsperada = CervejaDTOBuilder.builder().build().toCervejaDTO();

        //when
        when(cervejaRepository.findById(cervejaDTODeletadaEsperada.getId()))
                .thenReturn(Optional.empty());

        //then
        Assertions.assertThrows(CervejaNaoEncontradaException.class,
                () -> cervejaService.deleteById(cervejaDTODeletadaEsperada.getId()));
    }
}