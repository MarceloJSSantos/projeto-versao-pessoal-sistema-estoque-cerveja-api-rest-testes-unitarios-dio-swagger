package com.marcelojssantos.dio.estoquecerveja.controller;

import com.marcelojssantos.dio.estoquecerveja.builder.CervejaDTOBuilder;
import com.marcelojssantos.dio.estoquecerveja.dto.CervejaDTO;
import com.marcelojssantos.dio.estoquecerveja.exception.CervejaNaoEncontradaException;
import com.marcelojssantos.dio.estoquecerveja.service.CervejaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import static com.marcelojssantos.dio.estoquecerveja.utils.utilitarioConverteJson.comoJsonString;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CervejaControllerTest {

    private static final String CERVEJA_API_URL_PATH = "/api/v1/cervejas";
    //private static final long CERVEJA_VALIDA_ID = 1L;
    //private static final long CERVEJA_INVALIDA_ID = 2L;
    //private static final String CERVEJA_API_URL_SUBPATH_INCREMENTA = "/incrementa";
    //private static final String CERVEJA_API_URL_SUBPATH_DECREMENTA = "/decrementa";

    @InjectMocks
    private CervejaController cervejaController;

    @Mock
    private CervejaService cervejaService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(cervejaController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void quandoPOSTEChamadoCervejaEInserida() throws Exception {
        //given
        CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().toCervejaDTO();

        // when
        when(cervejaService.insert(cervejaDTO)).thenReturn(cervejaDTO);

        //then
        mockMvc.perform(post(CERVEJA_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(comoJsonString(cervejaDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome", is(cervejaDTO.getNome())))
                .andExpect(jsonPath("$.marca", is(cervejaDTO.getMarca())))
                .andExpect(jsonPath("$.tipo", is(cervejaDTO.getTipo().toString())));
    }

    @Test
    void quandoPOSTEChamadoSemCampoRequeridoErroERetornado() throws Exception {
        //given
        CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().toCervejaDTO();
        cervejaDTO.setMarca(null);

        //then
        mockMvc.perform(post(CERVEJA_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(comoJsonString(cervejaDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void quandoGETEChamadoComNomeValidoEntaoStatusOKERetornado() throws Exception {
        //given
        CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().toCervejaDTO();

        //when
        when(cervejaService.findByNome(cervejaDTO.getNome())).thenReturn(cervejaDTO);

        //then
        mockMvc.perform(get(CERVEJA_API_URL_PATH + "/" + cervejaDTO.getNome())
                .contentType(MediaType.APPLICATION_JSON)
                .content(comoJsonString(cervejaDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is(cervejaDTO.getNome())))
                .andExpect(jsonPath("$.marca", is(cervejaDTO.getMarca())))
                .andExpect(jsonPath("$.tipo", is(cervejaDTO.getTipo().toString())));
    }

    @Test
    void quandoGETEChamadoComNomeNaoEncontradoEntaoNaoEncontradoERetornado() throws Exception {
        //given
        CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().toCervejaDTO();

        //when
        when(cervejaService.findByNome(cervejaDTO.getNome()))
                .thenThrow(CervejaNaoEncontradaException.class);

        //then
        mockMvc.perform(get(CERVEJA_API_URL_PATH + "/" + cervejaDTO.getNome())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}